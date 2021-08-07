import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

public class Counter extends Actor
{
    private int value = 0;
    private String text;

    /**
     * Create a counter without a text prefix, initialized to zero.
     */
    public Counter()
    {
        this("");
    }

    /**
     * Create a counter with a given text prefix, initialized to zero.
     */
    public Counter(String prefix)
    {
        text = prefix;
        int imageWidth= (text.length() + 2) * 10;
        setImage(new GreenfootImage(imageWidth, 16));
        updateImage();
    }
    
    public int getValue(){
        return value;
    }
    
    public void setValue(int value){
        this.value=value;
    }
    
    public void countAnts(int secondValue){
        updateImage1(secondValue);
    }

    /**
     * Increment the counter value by one.
     */
    public void draw()
    {
         updateImage();
    }

    /**
     * Show the current text and count on this actor's image.
     */
    private void updateImage()
    {
        GreenfootImage image = getImage();
        image.clear();
        image.drawString(text + value, 1, 12);
    }
    
    private void updateImage1(int secondValue)
    {
        GreenfootImage image = getImage();
        image.clear();
        image.drawString(text + value+"(" + secondValue + ")", 1, 12);
    }
}