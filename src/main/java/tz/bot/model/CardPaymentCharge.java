package tz.bot.model;

public class CardPaymentCharge {

    private boolean isCardDebited;

    public CardPaymentCharge(boolean isCardDebited) {
        this.isCardDebited = isCardDebited;
    }

    public boolean isCardDebited() {
        return isCardDebited;
    }

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "isCardDebited=" + isCardDebited +
                '}';
    }
}
