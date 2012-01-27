package edgruberman.bukkit.simpletemplate.commands.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class to consolidate/organize common code for interacting with command line parameters.
 */
public class Parser {

    /**
     * Parse a list of long numbers from the command line in comma delimited form.
     * 
     * @param context command execution context
     * @param position position in command arguments (0 based, not including leading command label)
     * @return list of each number that was delimited by a comma; null if argument does not exist
     */
    public static List<Long> parseLongList(final Context context, final int position) {
        if (context.arguments.size() <= position) return null;

        List<Long> values = new ArrayList<Long>();
        for (String s : context.arguments.get(position).split(","))
            try {
                values.add(Long.parseLong(s));
            } catch (Exception e) {
                continue;
            }

        return values;
    }

}