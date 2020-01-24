package foerstermann.kai.zitate;

public class Quote {

    private final String quoteText;
    private final String quoteAuthor;
    private final String imageId;

    public Quote(String quoteText, String quoteAuthor, String imageId) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
        this.imageId = imageId;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public String getImageId() {
        return imageId;
    }

    @Override
    public String toString() {
        String output = quoteText + " - " + quoteAuthor + " - ID: " + imageId;
        return output;
    }
}
