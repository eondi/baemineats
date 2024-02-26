package com.sparta.baemineats.dto.responseDto;
import com.sparta.baemineats.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponse {
    private String menuName;
    private int menuPrice;
    private String menuDescription;
    private String storeName;

    public MenuResponse(Menu saveMenu) {
        this.menuName = saveMenu.getMenuName();
        this.menuPrice = saveMenu.getMenuPrice();
        this.menuDescription = saveMenu.getMenuDescription();
        this.storeName = saveMenu.getStore().getStoreName();
    }
}
