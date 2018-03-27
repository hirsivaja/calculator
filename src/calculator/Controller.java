package calculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class Controller {
    @FXML
    private TextField decimal;
    @FXML
    private TextField hex;
    @FXML
    private TextField binary;
    @FXML
    private TextField octal;
    @FXML
    private TextArea base64;
    @FXML
    private TextArea hexString;
    @FXML
    private TextArea ascii;
    @FXML
    private TextArea ascii2;

    @FXML
    protected void decTyped(KeyEvent event) {
        intTyped(decimal, 10);
    }

    @FXML
    protected void hexTyped(KeyEvent event) {
        intTyped(hex, 16);
    }

    @FXML
    protected void binTyped(KeyEvent event) {
        intTyped(binary, 2);
    }

    @FXML
    protected void octalTyped(KeyEvent event) {
        intTyped(octal, 8);
    }

    @FXML
    protected void base64Typed(KeyEvent event) {
        try{
            stringTyped(base64, DatatypeConverter.parseBase64Binary(base64.getText()));
        } catch (Exception e){
            base64.setStyle("-fx-background-color: red;");
        }
    }

    @FXML
    protected void hexStringTyped(KeyEvent event) {
        try{
            stringTyped(hexString, DatatypeConverter.parseHexBinary(hexString.getText()));
        } catch (Exception e){
            hexString.setStyle("-fx-background-color: red;");
        }
    }

    @FXML
    protected void asciiTyped(KeyEvent event) {
        try{
            stringTyped(ascii, ascii.getText().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            ascii.setStyle("-fx-background-color: red;");
        }
    }

    @FXML
    protected void ascii2Typed(KeyEvent event) {
        try{
            stringTyped(ascii2, ascii.getText().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            ascii2.setStyle("-fx-background-color: red;");
        }
    }

    private String hexToAscii(byte[] hex, boolean showNonPrintable){
        StringBuilder sb = new StringBuilder();
        for(byte b : hex){
            if(b >= 0x20 && b <= 0x7E){
                sb.append((char) b);
            } else {
                if(showNonPrintable){
                    sb.append('[');
                    if(b < 0){
                        sb.append(Integer.toHexString(b + 256).toUpperCase());
                    } else {
                        sb.append(Integer.toHexString(b).toUpperCase());
                    }
                    sb.append(']');
                } else {
                    sb.append(' ');
                }
            }
        }
        return sb.toString();
    }

    private void resetIntStyles() {
        decimal.setStyle("");
        hex.setStyle("");
        binary.setStyle("");
        octal.setStyle("");
    }

    private void resetStringStyles() {
        base64.setStyle("");
        hexString.setStyle("");
        ascii.setStyle("");
        ascii2.setStyle("");
    }

    private void intTyped(TextField current, int radix){
        String currentText = current.getText();
        int currentCaret = current.getCaretPosition();
        try{
            long dec = Long.parseLong(currentText, radix);
            decimal.setText(String.valueOf(dec));
            hex.setText(Long.toHexString(dec).toUpperCase());
            binary.setText(Long.toBinaryString(dec));
            octal.setText(Long.toOctalString(dec));
            resetIntStyles();
        } catch (NumberFormatException e){
            decimal.setText("");
            hex.setText("");
            binary.setText("");
            octal.setText("");
            if(!currentText.isEmpty()){
                current.setStyle("-fx-background-color: red;");
                current.setText(currentText);
            } else {
                resetIntStyles();
            }
        } finally {
            current.positionCaret(currentCaret);
        }
    }

    private void stringTyped(TextArea current, byte[] value){
        String currentText = current.getText();
        int currentCaret = current.getCaretPosition();
        base64.setText(DatatypeConverter.printBase64Binary(value));
        hexString.setText(DatatypeConverter.printHexBinary(value));
        ascii.setText(hexToAscii(value, false));
        ascii2.setText(hexToAscii(value, true));
        current.setText(currentText);
        current.positionCaret(currentCaret);
        resetStringStyles();
    }
}
