package net.jsaistudios.cpsc.cpsc_app.HomePage;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;

public class HomeObject extends ItemObject {
    private String body, date;

    public HomeObject() {
        super();
    }
    public HomeObject(String n, String bod, String dat) {
        super(n);
        body =bod;
        date = dat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
