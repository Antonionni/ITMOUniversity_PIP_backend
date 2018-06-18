package models.serviceEntities;

public class VerifyPassageItemModel {
    private int passageId;
    private boolean result;

    public VerifyPassageItemModel() {}

    public VerifyPassageItemModel(int passageId, boolean result) {
        this.passageId = passageId;
        this.result = result;
    }

    public int getPassageId() {
        return passageId;
    }

    public void setPassageId(int passageId) {
        this.passageId = passageId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
