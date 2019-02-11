import anonymization.DeIdentifier;
import initialization.DependencyBinder;

import java.util.LinkedList;

public class DummyClient {

    /**
     * TODO Implement End Point Entry Method
     */


    /**
     * TODO Delete this method after end point entry implementation
     * This method is merely to test the other layers
     */

    public static void main(String[] args) {
        LinkedList<String> verwendungszwecke = new LinkedList<String>();
        verwendungszwecke.add("Pascal Zoleko Lastschrift / REF Belastung Rate/004 Konto 12116122 Umweltsparv Ertrag Rate/Monatlich Einzug Zum 01 .06.2017 End-to-end-ref.: Rate/004 Konto 12116122 Core / Mandatsref.: Knd/211612/00000001 Gläubiger-id: De19zzz00000019221 Ref. J521715195638744/20452");
        verwendungszwecke.add("Drilon Recica Übertrag / Überweisung Lohn / Gehalt         06/17end-to-end-ref.: 7217204782-0001088lg0000 Ref. Jh217180f1035118/29868");
        verwendungszwecke.add("Nino Handler Lastschrift / Belastung Bhw 2214579 1 00 K Chc-17 End-to-end-ref.: Nicht Angegeben Core / Mandatsref.: 2214579100mv01 Gläubiger-id: De53bhw00000024235 Ref. Jm217271g2326339/22525");
        verwendungszwecke.add("Linus See Svwz+miete");
        verwendungszwecke.add("Isa Eref+ac717303-000780mde12017000003064488 Mref+01ca0000000000000000000000001639773 Cred+fr91deo477261 Svwz+vers.-nr. 75115140 Mawista Student");
        verwendungszwecke.add("Eref+250716283804513201256006915 Mref+5600691585121807251628cred+de58zzz00000257236 Svwz+250716283804513201256006915 Rewe Sagt Danke. 46400517");
        verwendungszwecke.add("Entgeltinformation Siehe Anlage");
        verwendungszwecke.add("Eref+pnr566792 ,knr75853 Mref+75853 Cred+de65hlz00000896922 Svwz+pnr566792 ,knr75853 Verwaltungspauschale 04.01.2017 18,00 Eur Fitone");
        verwendungszwecke.add("Lastschrift / Belastung Kartenzahlung 3151 Ec Stengel Fuerth//Fuerth/De 2017-07-24t18:34:20 Kfn 2  Vj 2012 Ref. 6al17206d1135125/4799");
        verwendungszwecke.add("Auszahlung Gaa Ga Nr00000687 Blz37040044 205.08/16.54uhr Bergis  27 Eur     200,00 Geb.eur 0,00ref. 28a17217i2936971/38703");
        verwendungszwecke.add("Auszahlung Gaa Auszahlung Postbank//Fuerth/De 2017-08-27t12:49:37 Kfn 2  Vj 2012 Ref. 2fb17240c1959217/18051");
        verwendungszwecke.add("Lastschrift / Belastung Pp.1996.pp . Dandjelkovi, Ihr Einka Uf Bei Dandjelkovi End-to-end-ref.: 1001569886335 Pp.1996.pp Paypal Core / Mandatsref.: 5w7j224mcstnw Gläubiger-id: Lu96zzz0000000000000000058 Ref. Hq21723692245918/665");
        verwendungszwecke.add("Lastschrift / Belastung Logpay Onlineticket I.a.v. Vag Verk Ehrs Ag Nuernberg. Ihre Kundennr. 1 608060113 End-to-end-ref.: Zaa0030171134 Core / Mandatsref.: 1005427437 Gläubiger-id: De90lpy00000046849 Ref. J0217283g3717528/162");
        verwendungszwecke.add("Lastschrift / Belastung Pp.1996.pp . Takeawaycom, Ihr Einka Uf Bei Takeawaycom End-to-end-ref.: 1001287075535 Pp.1996.pp Paypal Core / Mandatsref.: 5w7j224mcstnw Gläubiger-id: Lu96zzz0000000000000000058 Ref. Iu21717993433269/4739");
        verwendungszwecke.add("Lastschrift / Belastung Kartenzahlung 3016 Ec Schuler Fuerth//Fuerth/De 2017-07-01t18:47:22 Kfn 2  Vj 2012 Ref. 11b17184f2010164/41318");
        verwendungszwecke.add("SVWZ+2017-08-03t12.27.24 Karte0 2019-12 Abwa+6850 Edeka//Nuernberg/De");
        verwendungszwecke.add("03.11/11.37uhr Sulzbacher");
        verwendungszwecke.add("Eref+(kbh407 9115155)mihv2536029739 Svwz+foer.-nr.. 519000310437754 Bafoeg Auszahlung 2018021 407/911515-5/1503 68181 -0amt Fuer Ausbildungsfoerderung E");
        verwendungszwecke.add("Eref+1002606185140 Pp.3769.pp Paypal Mref+4552224mzk52c Cred+lu96zzz0000000000000000058 Svwz+pp.3769.pp . Flixbus, Ihr Einkauf Bei Flixbus");

        DeIdentifier deIdentifier = DependencyBinder.getDeIdentifier();
        for (String transaction : verwendungszwecke) {
            System.out.println(deIdentifier.getDeIdentifiedText(transaction));
        }
    }
}
