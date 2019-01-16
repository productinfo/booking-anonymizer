import anonymization.DeIdentifier;
import initialization.DependencyBinder;

import java.util.LinkedList;
import java.util.List;

public class DummyClient {

    /**
     * TODO Implement End Point Entry Method
     */


    /**
     * TODO Delete this method after end point entry implementation
     * This method is merely to test the other layers
     */

    public static void main(String[] args){

        //Test data
        LinkedList<String> verwendungszwecke = new LinkedList<String>();
        verwendungszwecke.add("Lastschrift / Belastung Rate/004 Konto 12116122 Umweltsparv Ertrag Rate/Monatlich Einzug Zum 01 .06.2017 End-to-end-ref.: Rate/004 Konto 12116122 Core / Mandatsref.: Knd/211612/00000001 Gläubiger-id: De19zzz00000019221 Ref. J521715195638744/20452");
        verwendungszwecke.add("Übertrag / Überweisung Lohn / Gehalt         06/17end-to-end-ref.: 7217204782-0001088lg0000 Ref. Jh217180f1035118/29868");
        verwendungszwecke.add("Lastschrift / Belastung Bhw 2214579 1 00 K Chc-17 End-to-end-ref.: Nicht Angegeben Core / Mandatsref.: 2214579100mv01 Gläubiger-id: De53bhw00000024235 Ref. Jm217271g2326339/22525");
        verwendungszwecke.add("Svwz+miete");
        verwendungszwecke.add("Eref+ac717303-000780mde12017000003064488 Mref+01ca0000000000000000000000001639773 Cred+fr91deo477261 Svwz+vers.-nr. 75115140 Mawista Student");
        verwendungszwecke.add("Eref+250716283804513201256006915 Mref+5600691585121807251628cred+de58zzz00000257236 Svwz+250716283804513201256006915 Rewe Sagt Danke. 46400517");
        verwendungszwecke.add("Entgeltinformation Siehe Anlage");
        verwendungszwecke.add("Eref+pnr566792 ,knr75853 Mref+75853 Cred+de65hlz00000896922 Svwz+pnr566792 ,knr75853 Verwaltungspauschale 04.01.2017 18,00 Eur Fitone");

        DeIdentifier deIdentifier = DependencyBinder.getAnonymizer();
        List<String> deIdentifiedVerwendungszwecks = deIdentifier.getDeIdentifiedText(verwendungszwecke);
        for(String deIdentifiedVerwendungszweck: deIdentifiedVerwendungszwecks) {
            System.out.println(deIdentifiedVerwendungszweck);
        }

    }
}
