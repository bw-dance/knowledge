package web.domain;

public class MessageState {
    private  String judge;
    private  String url;
    private  String urlName;
    public MessageState() {

    }
    public MessageState(String judge, String url, String urlName) {
        this.judge = judge;
        this.url = url;
        this.urlName = urlName;
    }

    @Override
    public String toString() {
        return "MassageState{" +
                "judge='" + judge + '\'' +
                ", url='" + url + '\'' +
                ", urlName='" + urlName + '\'' +
                '}';
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
