package com.sparta.baemineats.dto.responseDto;
import com.sparta.baemineats.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponse {
    private Long menuId;
    private String menuName;
    private int menuPrice;
    private String menuDescription;
    private String storeName;
    private String imageUrl;

    public MenuResponse(Menu saveMenu, String imageUrl) {
        this.menuId = saveMenu.getMenuId();
        this.menuName = saveMenu.getMenuName();
        this.menuPrice = saveMenu.getMenuPrice();
        this.menuDescription = saveMenu.getMenuDescription();
        this.storeName = saveMenu.getStore().getStoreName();
        this.imageUrl = imageUrl;
    }
}
