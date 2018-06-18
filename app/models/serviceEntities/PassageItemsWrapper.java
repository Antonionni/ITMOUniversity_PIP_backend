package models.serviceEntities;

import java.util.Collection;

public class PassageItemsWrapper {
    private Collection<PassageItem> passageItems;

    public PassageItemsWrapper() {}

    public PassageItemsWrapper(Collection<PassageItem> passageItems) {
        this.passageItems = passageItems;
    }

    public Collection<PassageItem> getPassageItems() {
        return passageItems;
    }

    public void setPassageItems(Collection<PassageItem> passageItems) {
        this.passageItems = passageItems;
    }
}
