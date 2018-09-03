package util;

import org.springframework.web.multipart.MultipartFile;

public class UploadedImageFile {
	//这里的属性名称img必须和分类页面中的增加分类部分中的type="file"的name值保持一致。
	MultipartFile img;

	public MultipartFile getImg() {
		return img;
	}

	public void setImg(MultipartFile img) {
		this.img = img;
	}
	
	
}
