package com.sparta.baemineats.dto.requestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MenuRequest {
    private String menuName;
    private int menuPrice;
    private String menuDescription;
    private MultipartFile image;

    public MenuRequest(String menuName, int menuPrice, String menuDescription, MultipartFile image) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
        this.image = image;
    }
}
