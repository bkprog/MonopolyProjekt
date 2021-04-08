package Source;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GridMapImage {
    private Image propImage;
    private ImageView propImageView;
    private String propertyNamePath;
    private String propertyOwner;

    public GridMapImage(int propertyNumber,String propertyOwner){
        this.propertyNamePath = "/images/Properties/property-" + propertyNumber + ".png";
        this.propertyOwner = propertyOwner;
        this.propImage = new Image(propertyNamePath);
        this.propImageView = new ImageView(propImage);
        this.propImageView.setFitWidth(75);
        this.propImageView.setFitHeight(112.5);
    }

    public ImageView getPropImageView(){
        return this.propImageView;
    }

    public String getPropertyOwner(){
        return this.propertyOwner;
    }

}
