/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.Map.Entry;

/**
 *
 * @author antonio
 */
public class SiglaProvincia implements Serializable{
    
public static final ImmutableMap<Object,Object> SIGLAPROVINCIA = ImmutableMap.builder()
        .put("AG","Agrigento")
        .put("AL","Alessandria")
        .put("AN","Ancona")
        .put("AO","Aosta")
        .put("AQ","L'Aquila")
        .put("AR","Arezzo")
        .put("AP","Ascoli-Piceno")
        .put("AT","Asti")
        .put("AV","Avellino")
        .put("BT","Barletta-Andria-Trani")
        .put("BL","Belluno")
        .put("BN","Benevento")
        .put("BG","Bergamo")
        .put("BI","Biella")
        .put("BO","Bologna")
        .put("BZ","Bolzano")
        .put("BS","Brescia")
        .put("BR","Brindisi")
        .put("CA","Cagliari")
        .put("CL","Caltanissetta")
        .put("CB","Campobasso")
        .put("CI","Carbonia Iglesias")
        .put("CE","Caserta")
        .put("CT","Catania")
        .put("CZ","Catanzaro")
        .put("CH","Chieti")
        .put("CO","Como")
        .put("CS","Cosenza")
        .put("CR","Cremona")
        .put("KR","Crotone")
        .put("CN","Cuneo")
        .put("EN","Enna")
        .put("FE","Ferrara")
        .put("FM","Fermo")
        .put("FI","Firenze")
        .put("FG","Foggia")
        .put("FC","Forli-Cesena")
        .put("FR","Frosinone")
        .put("GE","Genova")
        .put("GO","Gorizia")
        .put("GR","Grosseto")
        .put("IM","Imperia")
        .put("IS","Isernia")
        .put("SP","La-Spezia")
        .put("LT","Latina")
        .put("LE","Lecce")
        .put("LC","Lecco")
        .put("LI","Livorno")
        .put("LO","Lodi")
        .put("LU","Lucca")
        .put("MC","Macerata")
        .put("MN","Mantova")
        .put("MS","Massa Carrara")
        .put("MT","Matera")
        .put("VS","Medio Campidano")
        .put("ME","Messina")
        .put("MI","Milano")
        .put("MO","Modena")
        .put("MB","Monza Brianza")
        .put("NA","Napoli")
        .put("NO","Novara")
        .put("NU","Nuoro")
        .put("OG","Ogliastra")
        .put("OT","Olbia Tempio")
        .put("OR","Oristano")
        .put("PD","Padova")
        .put("PA","Palermo")
        .put("PR","Parma")
        .put("PV","Pavia")
        .put("PG","Perugia")
        .put("PU","Pesaro Urbino")
        .put("PE","Pescara")
        .put("PC","Piacenza")
        .put("PI","Pisa")
        .put("PT","Pistoia")
        .put("PN","Pordenone")
        .put("PZ","Potenza")
        .put("PO","Prato")
        .put("RA","Ravenna")
        .put("RG","Ragusa")
        .put("RC","Reggio Calabria")
        .put("RE","Reggio Emilia")
        .put("RI","Rieti")
        .put("RN","Rimini")
        .put("RM","Roma")
        .put("RO","Rovigo")
        .put("SA","Salerno")
        .put("SS","Sassari")
        .put("SV","Savona")
        .put("SI","Siena")
        .put("SR","Siracusa")
        .put("SO","Sondrio")
        .put("TA","Taranto")
        .put("TE","Teramo")
        .put("TR","Terni")
        .put("TO","Torino")
        .put("TP","Trapani")
        .put("TN","Trento")
        .put("TV","Treviso")
        .put("TS","Trieste")
        .put("UD","Udine")
        .put("VA","Varese")
        .put("VE","Venezia")
        .put("VB","Verbania")
        .put("VC","Vercelli")
        .put("VR","Verona")
        .put("VV","Vibo Valentia")
        .put("VI","Vicenza")
        .put("VT","Viterbo")
        .build();
        

public static ImmutableSet<Entry<Object,Object>> getEntrySet(){

        return SIGLAPROVINCIA.entrySet();
    }
}
