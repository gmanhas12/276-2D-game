package Background;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import Helpers.DefaultProperties;
import main.Panel;

/**
 * This file contains the class and function that handles drawing background textures and map generation
 * @author Tanzil Sarker
 * */
public class BackgroundController {
    public BackgroundTiles [] backgroundTiles;         //creates a background tile object, collision can be turned on and off
    public int mapTileMatrixReader[][];                //stores information of map matrix

    /**
     * constructor for the class
     * @param mapFileName string File name of the map
     */
    public BackgroundController(String mapFileName){
        //increase size depending on how many background texture file we have
        backgroundTiles= new BackgroundTiles[23];

        loadBackgroundTileImage();
        mapGen("/MapData/" + mapFileName + ".txt", DefaultProperties.MaxMapCol, DefaultProperties.MaxMapRow);
    }

    /**
     * Automates drawing of tiles
     * @param graphics2 Graphics2D for drawing the map
     */
    public void render(Graphics2D graphics2, Panel graphics){
        int mapCol = 0;
        int mapRow = 0;

        while(mapRow < DefaultProperties.MaxMapRow && mapCol < DefaultProperties.MaxMapCol) {
            int mapTileNumber= mapTileMatrixReader[mapCol][mapRow];

            // Get the adjusted X and Y positions on the board relative to the map
            int mapX = mapCol * DefaultProperties.adjustedTileSize;
            int mapY = mapRow * DefaultProperties.adjustedTileSize;

            // Gets the adjusted window position to draw the tile given the player is always centred on the window
            int windowX = mapX - graphics.player.mapX + graphics.player.getPlayerWindowPositionX();
            int windowY = mapY - graphics.player.mapY + graphics.player.getPlayerWindowPositionY();

            //draws tile on the given X and Y position
            graphics2.drawImage(backgroundTiles[mapTileNumber].image, windowX, windowY, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize, null);

            //moves 1 column right
            mapCol++;

            //if reaches right end of screen
            if(mapCol == DefaultProperties.MaxMapCol){
                //resets back to left side of column (like a type writer?)
                //now we are back to original starting position
                mapCol=0;
                //moving one row down
                mapRow++;
            }
        }
    }

    /**
     * Reads and store all the files in the image folder please, use the lines highlighted below to add new image files
     */
    private void loadBackgroundTileImage() {
        tiletool(0, false,"treelegacy");
        tiletool(1, true,"treelegacy");
        tiletool(2, true,"treelegacy");
        tiletool(3, true,"treelegacy");
        tiletool(4, false,"treelegacy");
        tiletool(5, false,"treelegacy");
        tiletool(6, true,"treelegacy");
        tiletool(7, true,"treelegacy");
        tiletool(8, true,"treelegacy");
        tiletool(9, true,"treelegacy");
        tiletool(10, false,"grass2");
        tiletool(11, true,"water");
        tiletool(12, true,"stone");
        tiletool(13, true,"tree");
        tiletool(14, false,"sand");
        tiletool(19, false,"waterdown");
        tiletool(20, false,"waterup");
        tiletool(21, false,"waterleft");
        tiletool(22, false,"waterright");
    }

    /**
     * Stores the image in the given path in the given index position of backgroundTiles
     * @param tile int index position for the image
     * @param coll boolean sets collision property for the tile
     * @param path string path to the image file
     */
    public void tiletool(int tile, boolean coll, String path){
        try{
            backgroundTiles[tile]=new BackgroundTiles();
            backgroundTiles[tile].image=ImageIO.read(getClass().getResourceAsStream("/MapTiles/"+path+".png"));
            backgroundTiles[tile].collide=coll;
        }
        catch (IllegalArgumentException | IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Makes map generation easier by reading a text file that contains a matrix representation of the map
     * Different values of matrix corresponds to different texture files to be loaded
     * @param mapFile String filepath for the txt file storing the map matrix
     * @param numColumns int number of columns in the file
     * @param numRows int number of rows in the file
     */
    public void mapGen(String mapFile, int numColumns, int numRows){
        try{
            InputStream fileReader= getClass().getResourceAsStream(mapFile);
            BufferedReader mapinfo = new BufferedReader(new InputStreamReader(fileReader));

            int column = 0;
            int row = 0;

            mapTileMatrixReader = new int[numColumns][numRows];
            while(column < numColumns && row < numRows){
                String currentRow = mapinfo.readLine();

                while(column < numColumns){
                    String value[] = currentRow.split(" ");
                    int digit = Integer.parseInt(value[column]);
                    mapTileMatrixReader[column][row] = digit;
                    column++;
                }

                if(column == numRows){
                    column=0;
                    row++;
                }
            }
            mapinfo.close();
        }
        catch(Exception ignored){}
    }
}
