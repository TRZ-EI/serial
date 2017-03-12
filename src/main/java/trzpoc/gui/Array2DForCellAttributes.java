package trzpoc.gui;

import java.awt.*;

public class Array2DForCellAttributes {
    private final CellAttributes[][] array;

    private int width, height;

    public Array2DForCellAttributes(int w , int h) {
        this.width = w;
        this.height = h;
        this.array = new CellAttributes[width][height];
    }
    public boolean exists(int x , int y) throws IndexOutOfBoundsException {
        boolean retValue = isInBoundaries(x, y);
         if (retValue){
             retValue &= (array[x][y] != null)? true: false;
         }
        return retValue;
    }
    public CellAttributes get( int x , int y ){
        return (this.isInBoundaries(x, y))? this.array[x][y]: null;
    }
    public void set( CellAttributes cellAttribute , int x , int y ) {
        if (this.isInBoundaries(x, y)){
            this.array[x][y] = cellAttribute;
        }
    }
    public void fillMatrix(){
        for (int r = 0; r < this.width; r ++){
            for (int c = 0; c < this.height; c++){
                this.array[r][c] = CellAttributes.getInstanceByDefault();
            }
        }
    }
    public void fillMatrixByColor(Color color){
        for (int r = 0; r < this.width; r ++){
            for (int c = 0; c < this.height; c++){
                this.array[r][c] = CellAttributes.getInstanceByColor(color);
            }
        }
    }
    private boolean isInBoundaries(int x, int y) {
        boolean retValue = (x < this.width && x >= 0)? true: false;
        retValue &= (y < this.height && y >=0)? true: false;
        return retValue;
    }
}