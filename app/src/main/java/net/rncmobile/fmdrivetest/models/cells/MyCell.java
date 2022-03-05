package net.rncmobile.fmdrivetest.models.cells;

import java.util.Objects;

import cz.mroczis.netmonster.core.model.Network;
import cz.mroczis.netmonster.core.model.band.BandLte;
import cz.mroczis.netmonster.core.model.band.BandWcdma;
import cz.mroczis.netmonster.core.model.cell.CellGsm;
import cz.mroczis.netmonster.core.model.cell.CellLte;
import cz.mroczis.netmonster.core.model.cell.CellNr;
import cz.mroczis.netmonster.core.model.cell.CellWcdma;
import cz.mroczis.netmonster.core.model.cell.ICell;
import cz.mroczis.netmonster.core.model.connection.IConnection;
import cz.mroczis.netmonster.core.model.connection.PrimaryConnection;
import cz.mroczis.netmonster.core.model.signal.SignalLte;
import cz.mroczis.netmonster.core.model.signal.SignalWcdma;

public class MyCell implements IMyCell {
    private boolean lteCa = false;
    private boolean lteCa2 = false;
    private boolean lteCa3 = false;

    private float agregLteRsrp;
    private String agregLteEarfcn;

    private float agreg2LteRsrp;
    private String agreg2LteEarfcn;

    private float agreg3LteRsrp;
    private String agreg3LteEarfcn;

    private ICell iCell;

    private int[] bw;

    private int mncOpe = -1;
    private int technology = -1;
    private int psc700 = -1;

    public MyCell() {
    }

    public MyCell(IMyCell myCell) {
        this.iCell = myCell.getICell();
    }

    @Override
    public int getMcc() {
        if(iCell != null && iCell.getNetwork() != null)
            return Integer.parseInt(iCell.getNetwork().getMcc());
        else return -1;
    }

    @Override
    public int getMnc() {
        if(iCell != null && iCell.getNetwork() != null)
            return Integer.parseInt(iCell.getNetwork().getMnc());
        else return -1;
    }

    @Override
    public int getMncOpe() {
        return mncOpe;
    }

    @Override
    public void setMncOpe(int mncOpe) {
        this.mncOpe = mncOpe;
    }

    public boolean isAutoriseToAttrib() {
        try {
            if (iCell.getBand() != null && iCell.getSignal() != null) {
                int freq = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(iCell.getBand()).getName()));
                int signal = 0;
                if (iCell.getSignal() != null)
                    Objects.requireNonNull(iCell.getSignal()).getDbm();

                return (getTech() != 3 || freq != 900 || signal >= -90.0)
                        && (getTech() != 3 || freq != 2100 || signal >= -90.0)
                        && (getTech() != 4 || freq != 700 || signal >= -90.0)
                        && (getTech() != 4 || freq != 1800 || signal >= -90.0)
                        && (getTech() != 4 || freq != 2600 || signal >= -90.0);
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAuthorizedNetwork() {
        if (iCell != null && iCell.getNetwork() != null) {
            return (iCell.getNetwork().getMcc().equals("208") && (iCell.getNetwork().getMnc().equals("15") || iCell.getNetwork().getMnc().equals("16")) // Free
                    || (iCell.getNetwork().getMcc().equals("647") && iCell.getNetwork().getMnc().equals("02")) // Mayotte
                    || (iCell.getNetwork().getMcc().equals("647") && iCell.getNetwork().getMnc().equals("03")));
        } else return false;
    }

    public boolean isRightMaskFreeMobile() {
        int rnc = getRnc();
        int cid = getCid();
        // Free
        if (getTech() == 2 && ((getMcc() == 208 && (getMncOpe() == 15 || getMncOpe() == 16)) || getMcc() == 647))
            return true;
        if (getTech() == 3 && rnc > 1000 && cid > 100/* || ((cid > 90 && cid < 95) || (cid > 20 && cid < 25)))*/)
            return true;
        if (getTech() == 4 && rnc >= 301001 && rnc <= 330000 && cid > 50 && cid < 55)
            return true;
        if (getTech() == 4 && rnc >= 401001 && cid > 50 && cid < 85)
            return true;
        if (getTech() == 5 && rnc >= 501001 && ((cid > 7000 && cid < 7004) || (cid > 3500 && cid < 3504)))
            return true;

        // Re
        if (getTech()  == 3 && cid > 40000 && cid < 60000 && rnc == 74)
            return true;
        if (getTech() == 4 && cid > 80 && cid < 85 && rnc >= 1 && rnc <= 300)
            return true;
        if (getTech()  == 4 && cid > 180 && cid < 185 && rnc >= 1 && rnc <= 300)
            return true;

        if(isZB() || is4GZB() || isFemto())
            return true;

        return getTech() == 4 && cid > 180 && cid < 185 && rnc >= 3001 && rnc <= 3300;
    }

    public int getRnc() {
        try {
            if (iCell instanceof CellGsm) return getBsic();
            if (iCell instanceof CellWcdma && ((CellWcdma) iCell).getRnc() != null) {
                /*
                if (!isZB() && isMonaco() && getStdRnc() >= 256 && getStdRnc() <= 1000 && getMcc() == 208 && (getMnc() == 15 || getMnc() == 16)) return getExtRnc();
                else return getStdRnc();
                 */
                return getStdRnc();
            }
            if (iCell instanceof CellLte && ((CellLte) iCell).getEnb() != null) return ((CellLte) iCell).getEnb();
            //if (iCell instanceof CellNr && ((CellNr) iCell).getNci() != null) return ((CellNr) iCell).getPci(); // Voir décodage natif du nm core
            if (iCell instanceof CellNr) {
                return 0;
            }
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    public int getCid() {
        try {
            if (iCell instanceof CellGsm && ((CellGsm) iCell).getCid() != null) return ((CellGsm) iCell).getCid();
            if (iCell instanceof CellWcdma && ((CellWcdma) iCell).getCid() != null) {
                /*
                if (!isZB() && isMonaco() && getStdRnc() >= 256 && getStdRnc() <= 1000 && getMcc() == 208 && (getMnc() == 15 || getMnc() == 16)) return getExtCid();
                else return getStdCid();
                 */
                return getStdCid();
            }
            else if (iCell instanceof CellLte && ((CellLte) iCell).getCid() != null) return ((CellLte) iCell).getCid();
            //if (iCell instanceof CellNr && ((CellNr) iCell).getNci() != null) return ((CellNr) iCell).getPci(); // Voir décodage natif du nm core
            else if (iCell instanceof CellNr) {
                return 0;
            }
            else return -1;
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
    }

    public String getRealRnc() {
        String rnc = String.valueOf(getRnc());
        if(rnc.length() > 5) {
            switch (rnc.substring(0, 2)) {
                case "40":
                case "50":
                    return rnc.substring(2);
                case "41":
                case "51":
                    return rnc.substring(1);
            }
        }
        return rnc;
    }

    @Override
    public int getBsic() {
        if(iCell instanceof CellGsm && ((CellGsm) iCell).getBsic() != null) return ((CellGsm) iCell).getBsic();
        return -1;
    }

    public boolean isZB() {
        boolean zb = false;
        if((getStdRnc() >= 976 && getStdRnc() <= 978)
                || (getXac() >= 3991 && getXac() <= 3993))
            zb = true;
        return zb;
    }

    public boolean is4GZB() {
        boolean zb = false;
        if(getXac() > 60000) zb = true;
        if (getLcid() >= 260000000 && getLcid() <  270000000) zb = true;
        return zb;
    }

    public boolean isMonaco() {
        boolean zb = false;
        if(getXac() == 3990) zb = true;
        return zb;
    }

    public boolean isFemto() {
        if(String.valueOf(getXac()).length() < 3) return false;
        return String.valueOf(getXac()).substring(0, 3).contains("398");
    }

    public int getSect() {
        int cid = getCid();

        if(cid > 20 && cid < 95) {
            if(String.valueOf(cid).charAt(1) == '1') return 1;
            if(String.valueOf(cid).charAt(1) == '2') return 2;
            if(String.valueOf(cid).charAt(1) == '3') return 3;
            if(String.valueOf(cid).charAt(1) == '4') return 4;
        }
        if((cid > 7000 && cid < 7004) || (cid > 3500 && cid < 3504)) {
            if(String.valueOf(cid).charAt(3) == '1') return 1;
            if(String.valueOf(cid).charAt(3) == '2') return 2;
            if(String.valueOf(cid).charAt(3) == '3') return 3;
            if(String.valueOf(cid).charAt(3) == '4') return 4;
        }

        // Re
        if(cid > 100 && cid < 200) {
            if(String.valueOf(cid).charAt(2) == '1') return 1;
            if(String.valueOf(cid).charAt(2) == '2') return 2;
            if(String.valueOf(cid).charAt(2) == '3') return 3;
            if(String.valueOf(cid).charAt(2) == '4') return 4;
        }

        return 0;
    }

    public String getNetworkTechnoTxt() {
        if(getTechnology() == (Integer.MAX_VALUE - 3)) return "4G+5G";
        if(getTechnology() == (Integer.MAX_VALUE - 2)) return "4G++5G";
        if (getTech() == 2) return "2G"; //"EDGE";
        if (getTech() == 3) return "3G"; //"UMTS";
        if(getTech() == 4) {
            StringBuilder tech = new StringBuilder("4G");
            if(this.bw != null && this.bw.length > 1) {
                for(int i=1; i<this.bw.length; i++) tech.append("+");
            }
            return tech.toString();
        }
        /*
        if (getTech() == 4 && !lteCa && !lteCa2) return "4G"; //"LTE";
        if (getTech() == 4 && lteCa && !lteCa2) return "4G+"; //"LTE-CA";
        if (getTech() == 4 && lteCa && lteCa2) return "4G++"; //"LTE-CA";
        if (getTech() == 4 && lteCa && lteCa2 && lteCa3) return "4G+++"; //"LTE-CA";
        */

        if (getTech() == 5) return "5G";

        return "-";
    }

    @Override
    public int getTech() {
        try {
            if (iCell instanceof CellGsm) return 2;
            if (iCell instanceof CellWcdma) return 3;
            if (iCell instanceof CellLte) return 4;
            if (iCell instanceof CellNr) return 5;
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    @Override
    public int getTechDetect() {
        try {
            if (iCell instanceof CellGsm) return 2;
            if (iCell instanceof CellWcdma) return 3;
            if(getTechnology() == (Integer.MAX_VALUE - 3)) return 45;
            if(getTechnology() == (Integer.MAX_VALUE - 2)) return 45;
            if (iCell instanceof CellLte) return 4;
            if (iCell instanceof CellNr) return 5;
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    public String getFreq() {
        try {
            if(!iCell.getBand().getName().equals("")) return iCell.getBand().getName();
            else return "-1";
        } catch (NullPointerException e) {
            return "0";
        } catch (Exception e) {
            return "-3";
        }
    }

    protected int getStdCid() {
        return (int)getLcid() & 0xffff;
    }
    /*
    protected int getExtCid() {
        return (int)getLcid() & 0xfff;
    }*/

    protected int getStdRnc() {
        return (int)(getLcid() >> 16) & 0xffff;
    }
    /*
    protected int getExtRnc() {
        return (int)(getLcid() >> 12) & 0xffff;
    }*/

    public long getLcid() {
        try {
            if (iCell instanceof CellGsm && ((CellGsm) iCell).getCid() != null) return ((CellGsm) iCell).getCid();
            if (iCell instanceof CellWcdma && ((CellWcdma) iCell).getCi() != null) return ((CellWcdma) iCell).getCi();
            if (iCell instanceof CellLte && ((CellLte) iCell).getEci() != null) return ((CellLte) iCell).getEci();
            if (iCell instanceof CellNr && ((CellNr) iCell).getNci() != null) return ((CellNr) iCell).getNci();
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    public ICell getICell() {
        return iCell;
    }

    public void setICell(ICell iCell) {
        this.iCell = iCell;
    }

    public float getMainSignal() {
        try {
            if (iCell instanceof CellGsm && ((CellGsm) iCell).getSignal().getDbm() != null) return ((CellGsm) iCell).getSignal().getDbm();
            if (iCell instanceof CellWcdma && ((CellWcdma) iCell).getSignal().getRssi() != null) return ((CellWcdma) iCell).getSignal().getDbm();
            if (iCell instanceof CellLte && ((CellLte) iCell).getSignal().getRsrp() != null) return (int)Math.round(((CellLte) iCell).getSignal().getRsrp());
            if (iCell instanceof CellNr && ((CellNr) iCell).getSignal().getSsRsrp() != null) return ((CellNr) iCell).getSignal().getSsRsrp();
            return iCell.getSignal().getDbm();
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
    }

    public float getSnr() {
        try {
            if((SignalLte)getICell().getSignal() != null && (getTechDetect() == 4 || getTechDetect() == 45) && ((SignalLte) getICell().getSignal()).getSnr() != null) {
                return ((SignalLte) getICell().getSignal()).getSnr().floatValue();
            } else return 0;
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
    }

    public int getTimingAdvance() {
        try {
            if(((SignalLte)getICell().getSignal() != null) && getTech() == 4 && ((SignalLte)getICell().getSignal()).getTimingAdvance() != null) {
                return ((SignalLte)getICell().getSignal()).getTimingAdvance();
            } else return 0;
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
    }

    @Override
    public int getXac() {
        try {
            if (iCell instanceof CellGsm && ((CellGsm) iCell).getLac() != null) return ((CellGsm) iCell).getLac();
            if (iCell instanceof CellWcdma && ((CellWcdma) iCell).getLac() != null) return ((CellWcdma) iCell).getLac();
            if (iCell instanceof CellLte && ((CellLte) iCell).getTac() != null) return ((CellLte) iCell).getTac();
            if (iCell instanceof CellNr && ((CellNr) iCell).getTac() != null) return  ((CellNr) iCell).getTac();
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    @Override
    public int getPxx() {
        try {
            if (iCell instanceof CellGsm && ((CellGsm) iCell).getNcc() != null) return ((CellGsm) iCell).getNcc();
            if (iCell instanceof CellWcdma && ((CellWcdma) iCell).getPsc() != null) return ((CellWcdma) iCell).getPsc();
            if (iCell instanceof CellLte && ((CellLte) iCell).getPci() != null) return ((CellLte) iCell).getPci();
            if (iCell instanceof CellNr && ((CellNr) iCell).getPci() != null) return  ((CellNr) iCell).getPci();
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    @Override
    public int getDownLink() {
        try {
            if (iCell instanceof CellGsm) return ((CellGsm) iCell).getBand().getArfcn() ;
            if (iCell instanceof CellWcdma) return ((CellWcdma) iCell).getBand().getDownlinkUarfcn();
            if (iCell instanceof CellLte) return Math.round(((CellLte) iCell).getBand().getDownlinkEarfcn());
            if (iCell instanceof CellNr) return Math.round(((CellNr) iCell).getBand().getDownlinkArfcn());
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    @Override
    public int getBandwith() {
        try {
            if (iCell instanceof CellGsm) return 0;
            if (iCell instanceof CellWcdma) return 0;
            if (iCell instanceof CellLte) return Math.round(((CellLte) iCell).getBandwidth());
            if (iCell instanceof CellNr) return 0;
        } catch (NullPointerException e) {
            return -2;
        } catch (Exception e) {
            return -3;
        }
        return -1;
    }

    @Override
    public int[] getBw() {
        return this.bw;
    }

    @Override
    public void setBw(int[] bw) {
        this.bw = bw;
    }

    @Override
    public int getTechnology() {
        return this.technology;
    }

    @Override
    public void setTechnology(int technology) {
        this.technology = technology;
    }

    @Override
    public void setPsc700(int psc) {
        this.psc700 = psc;
    }

    @Override
    public int getPsc700() {
        return this.psc700;
    }

    @Override
    public boolean isLteCa() {
        return lteCa;
    }

    public void setLteCa(boolean lteCa) {
        this.lteCa = lteCa;
    }

    public boolean isLteCa2() {
        return lteCa2;
    }

    public void setLteCa2(boolean lteCa2) {
        this.lteCa2 = lteCa2;
    }

    public float getAgregLteRsrp() {
        return agregLteRsrp;
    }

    public void setAgregLteRsrp(float agregLteRsrp) {
        this.agregLteRsrp = agregLteRsrp;
    }

    public String getAgregLteEarfcn() {
        return agregLteEarfcn;
    }

    public void setAgregLteEarfcn(String agregLteEarfcn) {
        this.agregLteEarfcn = agregLteEarfcn;
    }

    public float getAgreg2LteRsrp() {
        return agreg2LteRsrp;
    }

    public void setAgreg2LteRsrp(float agreg2LteRsrp) {
        this.agreg2LteRsrp = agreg2LteRsrp;
    }

    public String getAgreg2LteEarfcn() {
        return agreg2LteEarfcn;
    }

    public void setAgreg2LteEarfcn(String agreg2LteEarfcn) {
        this.agreg2LteEarfcn = agreg2LteEarfcn;
    }

    @Override
    public void setAgreg3LteRsrp(float agreg3LteRsrp) {
        this.agreg3LteRsrp = agreg3LteRsrp;
    }

    @Override
    public void setAgreg3LteEarfcn(String agreg3LteEarfcn) {
        this.agreg3LteEarfcn = agreg3LteEarfcn;
    }

    @Override
    public boolean isLteCa3() {
        return this.lteCa3;
    }

    @Override
    public void setLteCa3(boolean lteCa3) {
        this.lteCa3 = lteCa3;
    }

    @Override
    public float getAgreg3LteRsrp() {
        return this.agreg3LteRsrp;
    }

    @Override
    public String getAgreg3LteEarfcn() {
        return this.agreg3LteEarfcn;
    }
}
