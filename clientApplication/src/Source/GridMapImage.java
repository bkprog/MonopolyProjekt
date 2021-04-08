package Source;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class GridMapImage {
    private Image propImage;
    private ImageView propImageView;
    private String propertyNamePath;
    private String propertyOwner;
    private Label propertyInfo;
    private VBox box;

    public GridMapImage(int propertyNumber,String propertyOwner){
        box = new VBox();
        this.propertyNamePath = "/images/Properties/property-" + propertyNumber + ".png";
        this.propertyOwner = propertyOwner;
        this.propertyInfo = new Label(propertyOwner);
        this.propImage = new Image(propertyNamePath);
        this.propImageView = new ImageView(propImage);
        this.propImageView.setFitWidth(75);
        this.propImageView.setFitHeight(112.5);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(this.propImageView,propertyInfo);
    }

    public ImageView getPropImageView(){
        return this.propImageView;
    }

    public void setPropertyOwner(String propertyOwner){
        this.propertyOwner = propertyOwner;
    }

    public String getPropertyOwner(){
        return this.propertyOwner;
    }

    public VBox getBox(){
        return this.box;
    }

    public void setPropertyInfo(Label propertyOwner){
        this.propertyInfo.setText("");
        this.propertyInfo = propertyOwner;
    }

}
