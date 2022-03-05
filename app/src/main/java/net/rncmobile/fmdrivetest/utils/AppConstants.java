package net.rncmobile.fmdrivetest.utils;

import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.models.cells.MyCell;
import net.rncmobile.fmdrivetest.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cz.mroczis.netmonster.core.model.Network;
import cz.mroczis.netmonster.core.model.band.BandGsm;
import cz.mroczis.netmonster.core.model.band.BandLte;
import cz.mroczis.netmonster.core.model.band.BandNr;
import cz.mroczis.netmonster.core.model.band.BandWcdma;
import cz.mroczis.netmonster.core.model.cell.CellGsm;
import cz.mroczis.netmonster.core.model.cell.CellLte;
import cz.mroczis.netmonster.core.model.cell.CellNr;
import cz.mroczis.netmonster.core.model.cell.CellWcdma;
import cz.mroczis.netmonster.core.model.cell.ICell;
import cz.mroczis.netmonster.core.model.connection.IConnection;
import cz.mroczis.netmonster.core.model.connection.PrimaryConnection;
import cz.mroczis.netmonster.core.model.connection.SecondaryConnection;
import cz.mroczis.netmonster.core.model.signal.SignalGsm;
import cz.mroczis.netmonster.core.model.signal.SignalLte;
import cz.mroczis.netmonster.core.model.signal.SignalNr;
import cz.mroczis.netmonster.core.model.signal.SignalWcdma;

/**
 * Created by cedric_f25 25/12/17.
 */

public final class AppConstants {

    public static final boolean DEBUG_MODE = false;

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static boolean IS_APP_ALREADY_LAUNCH = false;
    public static boolean IS_APP_WAS_KILL = false;
    public static boolean DARK_MODE = false;

    public static boolean ACCESS_PERMISSION = false;

    public static int PERMISSION_COUNT = 0;

    public static final String DB_NAME = "rfm.db";
    public static final String PREF_NAME = "myPref";

    public static String version = "";

    public static int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static boolean screenOff = false;
    public static boolean cellRecorder = false;

    public static MainActivity mainActivity;

    public static int DEBUG_LCID_INDEX = 0;

    public static List<IMyCell> DEBUG_LCID = new ArrayList<>();

    public static IMyCell debugCell() {
        DEBUG_LCID.clear();

        Network networkFree = new Network("208", "15", "FR");
        Network networkOrange = new Network("208", "01", "FR");
        IConnection iConnection = new PrimaryConnection();

        BandGsm iBandGsm = new BandGsm(5600, "", 0);
        SignalGsm iSignalGsm = new SignalGsm(-70,0,80);

        BandWcdma iBandWcdma = new BandWcdma(5600, 1, "2600");
        SignalWcdma iSignalWcdma = new SignalWcdma(-70,90,80,70,5);

        BandLte iBandLte = new BandLte(5600, 1, "2600");
        SignalLte iSignalLte = new SignalLte(-70, -90.0, -80.0, -70, 10.0, 5);

        BandNr iBandNr = new BandNr(146000, 1, 1, "700");
        SignalNr iSignalNr = new SignalNr(-70, -90, -80, -70, 10, 5);

        // Mont vaudois 4G
        IMyCell cell = new MyCell();
        cell.setMncOpe(15);
        ICell iCell = new CellLte(networkFree, 104346429, 6808,51, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setTechnology((Integer.MAX_VALUE - 3));
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Héricourt
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 108033086, 6808,51, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setTechnology((Integer.MAX_VALUE - 3));
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Brevilliers fake
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 108800317, 6808,61, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setTechnology((Integer.MAX_VALUE - 3));
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        //2G
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellGsm(networkFree, 10880, 6808,61, iBandGsm, iSignalGsm, iConnection, 1, System.currentTimeMillis());
        //cell.setTechnology(2);
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Mont vaudois 3G Nouvelle numérotation
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkFree, 115034991, 3680,210, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Arveyres 4G
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 102832211, 2406,359, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setTechnology((Integer.MAX_VALUE - 3));
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Beychac 4G
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 105726289, 3300,138, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setTechnology((Integer.MAX_VALUE - 3));
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Mont vaudois 4G autocompletion
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 104346430, 6808,52, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Mont vaudois 5G autocompletion
        cell = new MyCell();
        cell.setMncOpe(15);
        long eci = 74616328;
        iCell = new CellNr(networkFree, eci, 6808,52, iBandNr, iSignalNr, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Mont vaudois 3G Ancienne numérotation
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkFree, 31141909, 3680,210, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // 3Gzb free
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkFree, 64104760, 3240,390, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);


        // 3Gzb
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkFree, 65104273, 3993,463, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Nouvelle 3Gzb
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkFree, 65104275, 3993,462, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // 4Gzb
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 268154881, 62224,290, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // 4Gzb 2
        cell = new MyCell();
        cell.setMncOpe(1);
        iCell = new CellLte(networkOrange, 268068099, 62212,138, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Nouvelle 4Gzb
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 268154891, 62223,290, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Monaco
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkFree, 31727628, 3990,48, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Itinérance
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellWcdma(networkOrange, 31727628, 4588,1, iBandWcdma, iSignalWcdma, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        // Réunion
        networkFree = new Network("647", "03", "FR");

        cell = new MyCell();
        cell.setMncOpe(3);
        iCell = new CellLte(networkFree, 54197, 740,39, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        cell = new MyCell();
        cell.setMncOpe(3);
        iCell = new CellLte(networkFree, 39095, 749,38, iBandLte, 10, iSignalLte, iConnection, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        DEBUG_LCID.add(cell);

        if(DEBUG_LCID_INDEX >= DEBUG_LCID.size())
            DEBUG_LCID_INDEX = 0;

        return DEBUG_LCID.get(DEBUG_LCID_INDEX);
    }

    public static List<IMyCell> debugCellSecondary() {
        List<IMyCell> debugSecondaryLcid = new ArrayList<>();

        Network networkFree = new Network("208", "15", "FR");
        IConnection iConnectionSecondary = new SecondaryConnection(false);

        BandLte iBandLte = new BandLte(5600, 1, "1800");
        SignalLte iSignalLte = new SignalLte(-70, -90.0, -80.0, -70, 10.0, 5);

        // Cell voisines
        // Mont vaudois 4G
        IMyCell cell = new MyCell();
        cell.setMncOpe(15);
        ICell iCell = new CellLte(networkFree, 104346430, 6808,51, iBandLte, 10, iSignalLte, iConnectionSecondary, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        debugSecondaryLcid.add(cell);

        // Mont vaudois 4G
        cell = new MyCell();
        cell.setMncOpe(15);
        iCell = new CellLte(networkFree, 104346431, 6808,51, iBandLte, 10, iSignalLte, iConnectionSecondary, 1, System.currentTimeMillis());
        cell.setICell(iCell);
        debugSecondaryLcid.add(cell);

        return debugSecondaryLcid;
    }

    private AppConstants() {
    }
}
