package indi.tshoiasc.wenuc_http.exec.storage.score;

public enum ScoreType {
    ACADEMY, DISCIPLINE, CLASS, USERNAME,
    AVAILABLE_SCORE, GRADE, INVALID, DATE, POINT, COURSE_TYPE, COURSE_NAME, COURSE_NUMBER, COURSE_PROPERTY, START_ACADEMY, TERM_YEAR, TEACHER,TERM,EXAM_PROPERTY
    ,COURSE_BACKGROUND, TERM_YEAR_FULL,TEACH_GRADE,TEACH_GRADE_POINT;
    public static String[] list(){
        return new String[]{
                "jgmc","zymc","bh_id","xh","bfzcj","cj","cjsfzf","dateDigitSeparator","jd","kclbmc","kcmc","kch","kcxzmc","kkbmmc","njdm_id","jsxm","kcbj","xnmmc","xqmmc","ksxz","xf","xfjd"
        };
    }
    @Override
    public String toString() {
        switch (this) {
            case TEACH_GRADE:
                return "xf";
            case ACADEMY:
                return "jgmc";
            case DISCIPLINE:
                return "zymc";
            case CLASS:
                return "bh_id";
            case USERNAME:
                return "xh";
            case AVAILABLE_SCORE:
                return "bfzcj";
            case GRADE:
                return "cj";
            case INVALID:
                return "cjsfzf";
            case DATE:
                return "dateDigitSeparator";
            case POINT:
                return "jd";
            case COURSE_TYPE:
                return "kclbmc";
            case COURSE_NAME:
                return "kcmc";
            case COURSE_NUMBER:
                return "kch";
            case COURSE_PROPERTY:
                return "kcxzmc";
            case START_ACADEMY:
                return "kkbmmc";
            case TERM_YEAR:
                return "njdm_id";
            case TEACHER:
                return "jsxm";
            case COURSE_BACKGROUND:
                return "kcbj";
            case TERM_YEAR_FULL:
                return "xnmmc";
            case TERM:
                return "xqmmc";
            case EXAM_PROPERTY:
                return "ksxz";
            case TEACH_GRADE_POINT:
                return "xfjd";
            default:
                return null;
        }

    }
}
