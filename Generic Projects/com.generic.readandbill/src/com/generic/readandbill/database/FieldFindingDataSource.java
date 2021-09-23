package com.generic.readandbill.database;

import java.util.ArrayList;
import java.util.List;

public class FieldFindingDataSource {

    private List<FieldFindings> ffs;

    public FieldFindingDataSource() {
        setFfs();
    }

    public List<FieldFindings> getFfs() {
        return ffs;
    }

    private void setFfs() {
        if (this.ffs != null){
            this.ffs.clear();
        } else {
            this.ffs = new ArrayList<FieldFindings>();
        }

        this.ffs.add(cursorToFieldFinding(new String[] {"0","none"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"B","Biting dog"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"L","Meter locked"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"N","Meter not found"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"H","Hard to read"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"E","Refused entry"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"I","Meter in house"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"C","Mtr removed-disc"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"V","Voluntary Disco"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"M","Meter defective"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"P","Pulled-out Meter"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"X","Demolished"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"F","Flat Rate"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"S","Street Lights"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"D","Disconnected"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"O","Others"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"1","Manual Billing"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"2","Interchanged"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"3","Mtr/Name corr."}));
        this.ffs.add(cursorToFieldFinding(new String[] {"5","Address corr."}));
        this.ffs.add(cursorToFieldFinding(new String[] {"6","Err. Multipler"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"7","New/Change mtr."}));
        this.ffs.add(cursorToFieldFinding(new String[] {"8","Err. Prev. rdng."}));
        this.ffs.add(cursorToFieldFinding(new String[] {"9","With Prev. rdng."}));
        this.ffs.add(cursorToFieldFinding(new String[] {"10","Flat Rate to Mtr"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"11","Mtr to Flat Rate"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"12","Old meter active"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"13","Not belong"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"15","No Meter Disconn"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"16","No mtr direct"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"17","Housed burnd/dem."}));
        this.ffs.add(cursorToFieldFinding(new String[] {"18","Mtr for relocatn"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"21","Defective meter"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"22","Hanging meter"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"24","Broken glass"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"25","No cover"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"26","Defective dial"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"27","Mtr dirty/blurrd"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"28","Mtr unreadable"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"29","Mtr transferred"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"30","Meter too high"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"31","No/Broken M.Seal"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"36","Tilted meter"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"40","For inspection"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"41","Tenant"}));
        this.ffs.add(cursorToFieldFinding(new String[] {"42","Owner"}));
    }

    public String getDescription(String code){
        String result = "";
        for (int i = 0; i < this.ffs.size(); i++){
            if (this.ffs.get(i).getfCode().equals(code)){
                result = ffs.get(i).getfDescription().toString();
                break;
            }
        }
        return result;
    }

    public String getCode(String description){
        String result = null;
        for (int i = 0; i < this.ffs.size(); i++){
            if (this.ffs.get(i).getfDescription().equals(description)){
                result = ffs.get(i).getfCode().toString();
                break;
            }
        }
        return result;
    }

    public String getCode(long id){
        String result = null;
        result = ffs.get((int) id).getfCode();
        return result;
    }

    private FieldFindings cursorToFieldFinding(String[] cursor){
        FieldFindings ff = new FieldFindings();
        ff.setfCode(cursor[0]);
        ff.setfDescription(cursor[1]);
        return ff;
    }

    public List<FieldFindings> getAllFieldFindings() {
        return ffs;
    }

}
