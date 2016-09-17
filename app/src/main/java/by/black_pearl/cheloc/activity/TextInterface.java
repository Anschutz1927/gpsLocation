package by.black_pearl.cheloc.activity;

/**
 * Interface to access to some methods for EditText and TextView.
 */
public interface TextInterface {
    void setText(CharSequence text);

    CharSequence getText();

    void setGravity(int gravity);

    void setEms (int ems);

    void setInputType (int type);
}
