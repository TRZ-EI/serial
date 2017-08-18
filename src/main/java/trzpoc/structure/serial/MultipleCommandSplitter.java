package trzpoc.structure.serial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 17/08/17
 * Time: 14.31
 */
public class MultipleCommandSplitter {
    public static MultipleCommandSplitter getNewInstance() {
        return new MultipleCommandSplitter();
    }
    private MultipleCommandSplitter(){

    }

    public List<String> splitMultipleCommand(String multipleCommand) {
        List<String> retValue = null;
        if (this.isPermittedCommand(multipleCommand)){
            int syncroPos = multipleCommand.indexOf('^');
            if (syncroPos >= 0) {
                multipleCommand = multipleCommand.substring(syncroPos + 1);
                String charToSplit = this.extractCharToSplit(multipleCommand);
                retValue = this.fillRetValueWithCommand(multipleCommand, charToSplit);
            }
        }else{
            retValue = new ArrayList<>();
            retValue.add(multipleCommand);
        }
        return retValue;
    }

    private boolean isPermittedCommand(String multipleCommand) {
        return (multipleCommand.indexOf("^V") >= 0 || multipleCommand.indexOf("^v") >= 0);
    }

    private List<String> fillRetValueWithCommand(String multipleCommand, String charToSplit) {
        List<String> retValue = new ArrayList<>();
        for (String command : multipleCommand.split(charToSplit)) {
            if (command.length() > 0) {
                retValue.add("^" + charToSplit + command);
            }
        }
        return retValue;
    }

    private String extractCharToSplit(String multipleCommand) {
        return (multipleCommand.indexOf("V") >= 0)? "V": "v";
    }
}
