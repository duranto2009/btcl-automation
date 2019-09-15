package officialLetter;

public enum OfficialLetterType {
    ADVICE_NOTE,
    DEMAND_NOTE,
    WORK_ORDER,
    EFR,
    LETTER;


    public static String getName(OfficialLetterType ol) {
        String res = "";
        switch (ol){
            case ADVICE_NOTE:
                res += "Advice Note";
                break;
            case DEMAND_NOTE:
                res += "Demand Note";
                break;
            case WORK_ORDER:
                res += "Work Order";
                break;
            case EFR:
                res += "EFR";
                break;
            case LETTER:
                res += "Letter";
                break;

        }
        return res;
    }
}

