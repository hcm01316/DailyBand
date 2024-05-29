package com.bnd.dailyband.domain;

public class Bandhr {
    private String MBR_ID;

    public String getMBR_ID() {
        return MBR_ID;
    }

    public void setMBR_ID(String MBR_ID) {
        this.MBR_ID = MBR_ID;
    }

    private String MBR_NCNM;
    private int MBR_AGE;
    private String MBR_GENDER;
    private String MBR_PREFER_AREA;
    private String MBR_ACT_REALM;
    private String MBR_PREFER_GENRE;
    private String MBR_PROFL_PHOTO;
    private int MBR_PRPT_STTUS;

    public String getMBR_NCNM() {
        return MBR_NCNM;
    }

    public void setMBR_NCNM(String MBR_NCNM) {
        this.MBR_NCNM = MBR_NCNM;
    }

    public int getMBR_AGE() {
        return MBR_AGE;
    }

    public void setMBR_AGE(int MBR_AGE) {
        this.MBR_AGE = MBR_AGE;
    }

    public String getMBR_GENDER() {
        return MBR_GENDER;
    }

    public void setMBR_GENDER(String MBR_GENDER) {
        this.MBR_GENDER = MBR_GENDER;
    }

    public String getMBR_PREFER_AREA() {
        return MBR_PREFER_AREA;
    }

    public void setMBR_PREFER_AREA(String MBR_PREFER_AREA) {
        this.MBR_PREFER_AREA = MBR_PREFER_AREA;
    }

    public String getMBR_ACT_REALM() {
        return MBR_ACT_REALM;
    }

    public void setMBR_ACT_REALM(String MBR_ACT_REALM) {
        this.MBR_ACT_REALM = MBR_ACT_REALM;
    }

    public String getMBR_PREFER_GENRE() {
        return MBR_PREFER_GENRE;
    }

    public void setMBR_PREFER_GENRE(String MBR_PREFER_GENRE) {
        this.MBR_PREFER_GENRE = MBR_PREFER_GENRE;
    }

    public String getMBR_PROFL_PHOTO() {
        return MBR_PROFL_PHOTO;
    }

    public void setMBR_PROFL_PHOTO(String MBR_PROFL_PHOTO) {
        this.MBR_PROFL_PHOTO = MBR_PROFL_PHOTO;
    }

    public int getMBR_PRPT_STTUS() {
        return MBR_PRPT_STTUS;
    }

    public void setMBR_PRPT_STTUS(int MBR_PRPT_STTUS) {
        this.MBR_PRPT_STTUS = MBR_PRPT_STTUS;
    }

    public String getMBR_PREFER_GENREK() {
        String gresult[]  = MBR_PREFER_GENRE.split(",");
        String genre = "";

        for ( String a : gresult)
        {
            if(a.equals("G01"))
                genre += "팝";
            else if (a.equals("G02"))
                genre += "발라드";
            else if (a.equals("G03"))
                genre += "인디음악";
            else if (a.equals("G04"))
                genre += "랩·힙합";
            else if (a.equals("G05"))
                genre += "K-POP";
            else if (a.equals("G06"))
                genre += "트로트";
            else if (a.equals("G07"))
                genre += "일렉트로닉";
            else if (a.equals("G08"))
                genre += "락";
            else if (a.equals("G09"))
                genre += "메탈";
            else if (a.equals("G10"))
                genre += "R&B";
            else if (a.equals("G11"))
                genre += "재즈";
            else if (a.equals("G12"))
                genre += "클래식";
            else if (a.equals("G13"))
                genre += "뮤지컬";
            else if (a.equals("G14"))
                genre += "국악";
            else if (a.equals("G15"))
                genre += "J-POP";
            else if (a.equals("G16"))
                genre += "월드뮤직";
            return genre;
        }


        if(genre.equals("G01"))
            genre = "팝";
        else if (genre.equals("G02"))
            genre = "발라드";
        else if (genre.equals("G03"))
            genre = "인디음악";
        else if (genre.equals("G04"))
            genre = "랩·힙합";
        else if (genre.equals("G05"))
            genre = "K-POP";
        else if (genre.equals("G06"))
            genre = "트로트";
        else if (genre.equals("G07"))
            genre = "일렉트로닉";
        else if (genre.equals("G08"))
            genre = "락";
        else if (genre.equals("G09"))
            genre = "메탈";
        else if (genre.equals("G10"))
            genre = "R&B";
        else if (genre.equals("G11"))
            genre = "재즈";
        else if (genre.equals("G12"))
            genre = "클래식";
        else if (genre.equals("G13"))
            genre = "뮤지컬";
        else if (genre.equals("G14"))
            genre = "국악";
        else if (genre.equals("G15"))
            genre = "J-POP";
        else if (genre.equals("G16"))
            genre = "월드뮤직";
        return genre;
    }

    public String getMBR_PREFER_AREAK() {
        String area = MBR_PREFER_AREA;
        if(area.equals("A01"))
            area = "서울";
        else if (area.equals("A02"))
            area = "경기";
        else if (area.equals("A03"))
            area = "부산";
        else if (area.equals("A04"))
            area = "대구";
        else if (area.equals("A05"))
            area = "광주";
        else if (area.equals("A06"))
            area = "대전";
        else if (area.equals("A07"))
            area = "충남";
        else if (area.equals("A08"))
            area = "충북";
        else if (area.equals("A09"))
            area = "세종";
        else if (area.equals("A10"))
            area = "울산";
        else if (area.equals("A11"))
            area = "인천";
        else if (area.equals("A12"))
            area = "강원";
        else if (area.equals("A13"))
            area = "전남";
        else if (area.equals("A14"))
            area = "전북";
        else if (area.equals("A15"))
            area = "경북";
        else if (area.equals("A16"))
            area = "광남";
        else if (area.equals("A17"))
            area = "제주";
        return area;
    }

    public String getMBR_ACT_REALMK() {
        String gresult[]  = MBR_ACT_REALM.split(",");
        String realm = "";
        for ( String a : gresult)
        {
            if(a.equals("R01"))
                realm += "기타/베이스 ";
            else if (a.equals("R02"))
                realm += "드럼 ";
            else if (a.equals("R03"))
                realm += "키보드 ";
            else if (a.equals("R04"))
                realm += "보컬 ";
            else if (a.equals("R05"))
                realm += "그 외 ";
        }
        return realm;
    }

}
