package com.owner.dgcverifier

import COSE.HeaderKeys
import com.upokecenter.cbor.CBORObject
import dgca.verifier.app.decoder.CertificateDecoder
import dgca.verifier.app.decoder.CertificateDecodingError
import dgca.verifier.app.decoder.CertificateDecodingResult
import dgca.verifier.app.decoder.base45.Base45Decoder
import dgca.verifier.app.decoder.base45.Base45Service
import dgca.verifier.app.decoder.base45.DefaultBase45Service
import dgca.verifier.app.decoder.cbor.DefaultGreenCertificateMapper
import dgca.verifier.app.decoder.cbor.GreenCertificateMapper
import dgca.verifier.app.decoder.compression.CompressorService
import dgca.verifier.app.decoder.compression.DefaultCompressorService
import dgca.verifier.app.decoder.cose.CryptoService
import dgca.verifier.app.decoder.cose.VerificationCryptoService
import dgca.verifier.app.decoder.cwt.CwtHeaderKeys
import dgca.verifier.app.decoder.model.CoseData
import dgca.verifier.app.decoder.model.GreenCertificate
import dgca.verifier.app.decoder.model.VerificationResult
import dgca.verifier.app.decoder.prefixvalidation.DefaultPrefixValidationService
import dgca.verifier.app.decoder.prefixvalidation.PrefixValidationService
import dgca.verifier.app.decoder.services.X509
import java.security.cert.Certificate
import java.util.zip.InflaterInputStream

class QrDecoder(private val base45Decoder: Base45Decoder, private val greenCertificateMapper: GreenCertificateMapper = DefaultGreenCertificateMapper()) :
    CertificateDecoder {
    companion object {
        const val PREFIX = "HC1:"
    }

    override fun decodeCertificate(qrCodeText: String): CertificateDecodingResult {
        val withoutPrefix: String =
            if (qrCodeText.startsWith(PREFIX)) qrCodeText.drop(PREFIX.length) else qrCodeText
        val base45Decoded: ByteArray = try {
            base45Decoder.decode(withoutPrefix)
        } catch (error: Throwable) {
            return CertificateDecodingResult.Error(
                CertificateDecodingError.Base45DecodingError(
                    error
                )
            )
        }

        val decompressed: ByteArray = try {
            base45Decoded.decompressBase45DecodedData()
        } catch (error: Throwable) {
            return CertificateDecodingResult.Error(
                CertificateDecodingError.Base45DecompressionError(
                    error
                )
            )
        }
        val coseData: CoseData = try {
            decompressed.decodeCose()
        } catch (error: Throwable) {
            return CertificateDecodingResult.Error(
                CertificateDecodingError.CoseDataDecodingError(
                    error
                )
            )
        }
        if (coseData.kid != null) {
            val publicKeys = DGCCertificateManager.keysFor(coseData.kid)
            if (publicKeys != null) {
                for (k in publicKeys) {
                    if (checkSignature(qrCodeText, k)) {
                        val greenCertificate: GreenCertificate = try {
                            coseData.cbor.decodeGreenCertificate()
                        } catch (error: Throwable) {
                            return CertificateDecodingResult.Error(
                                CertificateDecodingError.GreenCertificateDecodingError(
                                    error
                                )
                            )
                        }
                        return CertificateDecodingResult.Success(greenCertificate)
                    }
                }
            }
        }
        return CertificateDecodingResult.Error(CertificateDecodingError.EmptyGreenCertificate)
    }

    private fun checkSignature(prefix: String?, cert: Certificate): Boolean {
        val result = VerificationResult()
        val b45Service: Base45Service = DefaultBase45Service()
        val prefService: PrefixValidationService = DefaultPrefixValidationService()
        val compressorService: CompressorService = DefaultCompressorService()
        val base45 = prefService.decode(prefix!!, result)
        val compressed = b45Service.decode(base45, result)
        val cose: ByteArray = compressorService.decode(compressed, result)!!
        val cryptoService: CryptoService = VerificationCryptoService(X509())
        cryptoService.validate(cose, cert, result)
        return result.coseVerified
    }


    private fun ByteArray.decompressBase45DecodedData(): ByteArray {
        // ZLIB magic headers
        return if (this.size >= 2 && this[0] == 0x78.toByte() && (
                    this[1] == 0x01.toByte() || // Level 1
                            this[1] == 0x5E.toByte() || // Level 2 - 5
                            this[1] == 0x9C.toByte() || // Level 6
                            this[1] == 0xDA.toByte()
                    )
        ) {
            InflaterInputStream(this.inputStream()).readBytes()
        } else this
    }

    private fun ByteArray.decodeCose(): CoseData {
        val messageObject = CBORObject.DecodeFromBytes(this)
        val content = messageObject[2].GetByteString()
        val rgbProtected = messageObject[0].GetByteString()
        val rgbUnprotected = messageObject[1]
        val key = HeaderKeys.KID.AsCBOR()

        if (!CBORObject.DecodeFromBytes(rgbProtected).keys.contains(key)) {
            val objunprotected = rgbUnprotected.get(key).GetByteString()
            return CoseData(content, objunprotected)
        }
        val objProtected = CBORObject.DecodeFromBytes(rgbProtected).get(key).GetByteString()
        return CoseData(content, objProtected)
    }

    private fun ByteArray.decodeGreenCertificate(): GreenCertificate {
        val map = CBORObject.DecodeFromBytes(this)
        val hcert = map[CwtHeaderKeys.HCERT.asCBOR()]
        val cborObject = hcert[CBORObject.FromObject(1)]

        return greenCertificateMapper
            .readValue(cborObject)
    }
}