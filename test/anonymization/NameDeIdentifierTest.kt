package anonymization

import initialization.DependencyBinder
import named.entity.NamedEntityExtractorImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import preprocessing.PreprocessorImp
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@RunWith(BlockJUnit4ClassRunner::class)
class NameDeIdentifierTest {
    private lateinit var nameDeIdentifier: DeIdentifier

    @Before
    fun setup() {
        nameDeIdentifier = DependencyBinder.nameDeIdentifier
    }

    @Test
    fun `should return empty string on blank string`() {
        val anonymisedText = nameDeIdentifier.getDeIdentifiedText("   ")
        assertEquals("", anonymisedText)
    }

    @Test
    fun `text should not contain digits any more`() {
        val testData = listOf(
            "Impressionen Versand GmbH SOLADEST600 DE87600501010008683773 9991093962265 End-to-End-Ref.: CCB.304.UE.253988",
            "Florian Stefan Hirsch Rechtsschutz End-to-End-Ref.: NOTPROVIDED Kundenreferenz: NSCT1812180001050000000000000000007",
            "H+M 223 SAGT VIELEN DANK ELV66019269 22.12 14.13 ME3 End-to-End-Ref.: 66019269090147221218141346 Mandatsref: 6601926985491812221413 Gläubiger-ID: DE95ZZZ00001678202 SEPA-BASISLASTSCHRIFT einmalig",
            "Deut.Lebensvers.AG Allianz Vertrag DL-9454890306 RisikoLebensvers 01.01.19 - 31.01.19 End-to-End-Ref.: AB218354-E03118746 Mandatsref: LD09A000000001006633 Gläubiger-ID: DE77ZZZ00000063476 SEPA-BASISLASTSCHRIFT wiederholend",
            "OMV DEUTSCHLAND GMBH OMV D7540 Nürnberg 28.12.2018 12:26 End-to-End-Ref.: ELV91713100 28.12 12.26 ME3 Mandatsref: 9171310051291812281226 Gläubiger-ID: DE12ZZZ00000026622 SEPA-BASISLASTSCHRIFT wiederholend",
            "INTERLLOYD VERSICHE. INTERL VS.000613504 PRIVA EUR 9,23 End-to-End-Ref.: 0002499031 Mandatsref: DEM90000000278940 Gläubiger-ID: DE98ZZZ00000008196 SEPA-BASISLASTSCHRIFT wiederholend",
            "BAUHAUS NUERNBERG 291210173228037201201085930 ELV6524 4444 29.12 10.17 ME3 End-to-End-Ref.: 29121017322803720120108593065244444 Mandatsref: 6524444425031812291017 Gläubiger-ID: DE16ZZZ00000020245 SEPA-BASISLASTSCHRIFT einmalig",
            "Nicolas Dausch INGDDEFFXXX DE11500105175410502469 Miete + Nebenkosten End-to-End-Ref.: CCB.2.UE.164857",
            "E.ON Energie Deutschland VK 232041315388 Unschlittplatz 8 Nu ernberg ABRECHNUNG 11.01.19 2115208 25022 End-to-End-Ref.: B525002193386 Kundenreferenz: NSCT1901140000960000000000000000019",
            "ROSSMANN VIELEN DANK 160118373011039201220031580 ELV6526 9121 16.01 18.37 ME3 End-to-End-Ref.: 16011837301103920122003158065269121 Mandatsref: 6526912150661901161837 Gläubiger-ID: DE16ZZZ00000020245 SEPA-BASISLASTSCHRIFT einmalig",
            "Telekom Deutschland GmbH Festnetz Vertragskonto 5617504194 R G 7211228374/14.01.2019 End-to-End-Ref.: Zahlbeleg 336668377639 Mandatsref: DE000201000200000000000000001404316 Gläubiger-ID: DE93ZZZ00000078611 SEPA-BASISLASTSCHRIFT wiederholend",
            "ANITA EHLINGER Halle 3 End-to-End-Ref.: NOTPROVIDED Kundenreferenz: NSCT1901220000850000000000000000008",
            "Commerzbank AG LEISTUNGEN PER 01.02.2019, IBAN DE5 2670400310374553604, AZ R006401880, IN EUR: Tilgung 333,98 Zinsen 162, 14 End-to-End-Ref.: 31313448892019 Mandatsref: 301-0300-000000977465 Gläubiger-ID: DE3811000000020140 SEPA-BASISLASTSCHRIFT wiederholend",
            "ELENA MURIEL JESCHKE COBADEFFXXX DE36670400310374553601 SPAREN End-to-End-Ref.: NOTPROVIDED Dauerauftrag",
            "FOM Hochschule für Oekonomie + Mana gement gemeinnützige Gesellschaft m 15.08.17 303368018 252,86 End-to-End-Ref.: 0014440000DISK12578Z Mandatsref: FOM450160303368 Gläubiger-ID: DE69ZZZ00000693635 SEPA-BASISLASTSCHRIFT wiederholend",
            "Kartenzahlung KARSTADT LEBENSM. SAGT DANK//Nuernb 2019-02-09T16:58:19 KFN 3 VJ 2012",
            "StOk Bay f Finanzamt ERSTATT.240/488/03500 EST-VERANL. 1 7 End-to-End-Ref.: 240/488/03500 EST-G1302201905480169 Kundenreferenz: NSCT1902140006030000000000000000014",
            "LOGPAY FINANCIAL SERVICES GMBH LogPay OnlineTicket i.A.v. VAG Verk ehrs AG Nuernberg. Ihre Kundennr. 1 509010041 End-to-End-Ref.: ZAA0050671129 Mandatsref: 1016210922 Gläubiger-ID: DE90LPY00000046849 SEPA-BASISLASTSCHRIFT wiederholend",
            "adorsys GmbH + Co. KG Bew.Abr. 21.02 End-to-End-Ref.: NOTPROVIDED Kundenreferenz: NSCT1902220008740000000000000000002"
        )

        testData.forEach {
            val anonymisedText = nameDeIdentifier.getDeIdentifiedText(it)
            assertFalse { anonymisedText.contains("\\d".toRegex()) }
        }
    }

    @Test
    fun `text should not contain names or entities`() {
        val namedEntityExtractor = NamedEntityExtractorImpl(PreprocessorImp(), DependencyBinder.namedEntityRecognizers)
        val testData = listOf(
            "Impressionen Versand GmbH SOLADEST600 DE87600501010008683773 9991093962265 End-to-End-Ref.: CCB.304.UE.253988",
            "Florian Stefan Hirsch Rechtsschutz End-to-End-Ref.: NOTPROVIDED Kundenreferenz: NSCT1812180001050000000000000000007",
            "H+M 223 SAGT VIELEN DANK ELV66019269 22.12 14.13 ME3 End-to-End-Ref.: 66019269090147221218141346 Mandatsref: 6601926985491812221413 Gläubiger-ID: DE95ZZZ00001678202 SEPA-BASISLASTSCHRIFT einmalig",
            "Deut.Lebensvers.AG Allianz Vertrag DL-9454890306 RisikoLebensvers 01.01.19 - 31.01.19 End-to-End-Ref.: AB218354-E03118746 Mandatsref: LD09A000000001006633 Gläubiger-ID: DE77ZZZ00000063476 SEPA-BASISLASTSCHRIFT wiederholend",
            "OMV DEUTSCHLAND GMBH OMV D7540 Nürnberg 28.12.2018 12:26 End-to-End-Ref.: ELV91713100 28.12 12.26 ME3 Mandatsref: 9171310051291812281226 Gläubiger-ID: DE12ZZZ00000026622 SEPA-BASISLASTSCHRIFT wiederholend",
            "INTERLLOYD VERSICHE. INTERL VS.000613504 PRIVA EUR 9,23 End-to-End-Ref.: 0002499031 Mandatsref: DEM90000000278940 Gläubiger-ID: DE98ZZZ00000008196 SEPA-BASISLASTSCHRIFT wiederholend",
            "BAUHAUS NUERNBERG 291210173228037201201085930 ELV6524 4444 29.12 10.17 ME3 End-to-End-Ref.: 29121017322803720120108593065244444 Mandatsref: 6524444425031812291017 Gläubiger-ID: DE16ZZZ00000020245 SEPA-BASISLASTSCHRIFT einmalig",
            "Nicolas Dausch INGDDEFFXXX DE11500105175410502469 Miete + Nebenkosten End-to-End-Ref.: CCB.2.UE.164857",
            "E.ON Energie Deutschland VK 232041315388 Unschlittplatz 8 Nu ernberg ABRECHNUNG 11.01.19 2115208 25022 End-to-End-Ref.: B525002193386 Kundenreferenz: NSCT1901140000960000000000000000019",
            "ROSSMANN VIELEN DANK 160118373011039201220031580 ELV6526 9121 16.01 18.37 ME3 End-to-End-Ref.: 16011837301103920122003158065269121 Mandatsref: 6526912150661901161837 Gläubiger-ID: DE16ZZZ00000020245 SEPA-BASISLASTSCHRIFT einmalig",
            "Telekom Deutschland GmbH Festnetz Vertragskonto 5617504194 R G 7211228374/14.01.2019 End-to-End-Ref.: Zahlbeleg 336668377639 Mandatsref: DE000201000200000000000000001404316 Gläubiger-ID: DE93ZZZ00000078611 SEPA-BASISLASTSCHRIFT wiederholend",
            "ANITA EHLINGER Halle 3 End-to-End-Ref.: NOTPROVIDED Kundenreferenz: NSCT1901220000850000000000000000008",
            "Commerzbank AG LEISTUNGEN PER 01.02.2019, IBAN DE5 2670400310374553604, AZ R006401880, IN EUR: Tilgung 333,98 Zinsen 162, 14 End-to-End-Ref.: 31313448892019 Mandatsref: 301-0300-000000977465 Gläubiger-ID: DE3811000000020140 SEPA-BASISLASTSCHRIFT wiederholend",
            "ELENA MURIEL JESCHKE COBADEFFXXX DE36670400310374553601 SPAREN End-to-End-Ref.: NOTPROVIDED Dauerauftrag",
            "FOM Hochschule für Oekonomie + Mana gement gemeinnützige Gesellschaft m 15.08.17 303368018 252,86 End-to-End-Ref.: 0014440000DISK12578Z Mandatsref: FOM450160303368 Gläubiger-ID: DE69ZZZ00000693635 SEPA-BASISLASTSCHRIFT wiederholend",
            "Kartenzahlung KARSTADT LEBENSM. SAGT DANK//Nuernb 2019-02-09T16:58:19 KFN 3 VJ 2012",
            "StOk Bay f Finanzamt ERSTATT.240/488/03500 EST-VERANL. 1 7 End-to-End-Ref.: 240/488/03500 EST-G1302201905480169 Kundenreferenz: NSCT1902140006030000000000000000014",
            "LOGPAY FINANCIAL SERVICES GMBH LogPay OnlineTicket i.A.v. VAG Verk ehrs AG Nuernberg. Ihre Kundennr. 1 509010041 End-to-End-Ref.: ZAA0050671129 Mandatsref: 1016210922 Gläubiger-ID: DE90LPY00000046849 SEPA-BASISLASTSCHRIFT wiederholend",
            "adorsys GmbH + Co. KG Bew.Abr. 21.02 End-to-End-Ref.: NOTPROVIDED Kundenreferenz: NSCT1902220008740000000000000000002"
        )

        testData.forEach {
            val namedEntities = namedEntityExtractor.getNamedEntitiesAndPreprocessedText(it)
            if (namedEntities.isNotEmpty()) {
                val anonymisedText = nameDeIdentifier.getDeIdentifiedText(it)
                namedEntities.forEach {
                    assertFalse {
                        (nameDeIdentifier as? VZDeIdentifierImpl)?.sensibleNerTypes?.contains(it.value) == true
                            && anonymisedText.contains(it.key)
                    }
                }
            }
        }
    }

}