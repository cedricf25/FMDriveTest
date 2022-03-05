package net.rncmobile.fmdrivetest.models.cells;

import cz.mroczis.netmonster.core.model.cell.ICell;

public interface IMyCell {
    int getMcc();
    int getMnc();

    void setMncOpe(int mncOpe);
    int getMncOpe();

    long getLcid();
    int getTech();
    int getTechDetect();
    int getRnc();
    int getCid();
    int getXac();
    int getPxx();
    int getBsic();
    int getSect();
    int getDownLink();
    int getBandwith();
    String getFreq();

    String getRealRnc();
    String getNetworkTechnoTxt();

    float getMainSignal();
    float getSnr();
    int getTimingAdvance();

    boolean isAutoriseToAttrib();
    boolean isAuthorizedNetwork();
    boolean isRightMaskFreeMobile();

    boolean isZB();
    boolean is4GZB();
    boolean isFemto();

    int getTechnology();
    void setTechnology(int technology);

    ICell getICell();
    void setICell(ICell iCell);

    int[] getBw();
    void setBw(int[] bw);

    void setPsc700(int psc);
    int getPsc700();

    void setLteCa(boolean lteCa);
    boolean isLteCa();

    void setLteCa2(boolean lteCa2);
    boolean isLteCa2();

    void setLteCa3(boolean lteCa3);
    boolean isLteCa3();

    void setAgregLteRsrp(float agregLteRsrp);
    void setAgregLteEarfcn(String agregLteEarfcn);
    void setAgreg2LteRsrp(float agreg2LteRsrp);
    void setAgreg2LteEarfcn(String agreg2LteEarfcn);
    void setAgreg3LteRsrp(float agreg3LteRsrp);
    void setAgreg3LteEarfcn(String agreg3LteEarfcn);

    float getAgregLteRsrp();
    String getAgregLteEarfcn();
    float getAgreg2LteRsrp();
    String getAgreg2LteEarfcn();
    float getAgreg3LteRsrp();
    String getAgreg3LteEarfcn();
}
