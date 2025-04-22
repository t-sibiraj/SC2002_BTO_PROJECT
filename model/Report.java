package model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Report {
    private Map<Applicant, FlatBooking> info;

    public Report(HDBManager manager){
        info = new HashMap<>();
        manager.updateReport(this);
    }

    //getter function
    public Map<Applicant, FlatBooking> getInfo(){ return this.info; }

    public void addInfo(FlatBooking booking){
        info.put(booking.getApplication().getApplicant(), booking);
    }

    @Override
    public String toString() {
        if (info.isEmpty()){
            return "No reports";
        }

        return info.entrySet().stream()
            .map(entry -> "Applicant: " + entry.getKey().getName() + "\n" +entry.getValue().toString())
            .collect(Collectors.joining("\n","Reports\n","\n"));
    }
}
