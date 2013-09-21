package edgruberman.bukkit.bukkitplugin.commands.util;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.bukkit.configuration.ConfigurationSection;

/**
 * allows object references to be stored for lazy MessageFormat formatting
 *
 * @author EdGruberman (ed@rjump.com)
 * @version 1.7.0
 */
public class JoinList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    public static final String CONFIG_KEY_FORMAT = "format";
    public static final String CONFIG_KEY_ITEM = "item";
    public static final String CONFIG_KEY_DELIMITER = "delimiter";

    public static final String DEFAULT_FORMAT = "{0}";
    public static final String DEFAULT_ITEM = "{0}";
    public static final String DEFAULT_DELIMITER = " ";



    public static <T> JoinList<T> create(final Collection<? extends T> collection) {
        return JoinList.create(collection, JoinList.DEFAULT_DELIMITER);
    }

    public static <T> JoinList<T> create(final Collection<? extends T> collection, final String delimiter) {
        return JoinList.create(collection, delimiter, JoinList.DEFAULT_ITEM);
    }

    public static <T> JoinList<T> create(final Collection<? extends T> collection, final String delimiter, final String item) {
        return JoinList.create(collection, delimiter, item, JoinList.DEFAULT_FORMAT);
    }

    public static <T> JoinList<T> create(final Collection<? extends T> elements, final String delimiter, final String item, final String format) {
        final JoinList<T> result = new JoinList<T>(format, item, delimiter);
        result.addAll(elements);
        return result;
    }

    public static <T> JoinList<T> create(final Collection<? extends T> elements, final ConfigurationSection config, final String prefix) {
        final JoinList<T> result = new JoinList<T>(config, prefix);
        result.addAll(elements);
        return result;
    }



    private final String format;
    private final String item;
    private final String delimiter;

    public JoinList() {
        this(JoinList.DEFAULT_FORMAT, JoinList.DEFAULT_ITEM, JoinList.DEFAULT_DELIMITER);
    }

    public JoinList(final String format, final String item, final String delimiter) {
        this.format = format;
        this.item = item;
        this.delimiter = delimiter;
    }

    public JoinList(final ConfigurationSection config) {
        this(config, "");
    }

    public JoinList(final ConfigurationSection config, final String prefix) {
        this(config, prefix, JoinList.DEFAULT_FORMAT, JoinList.DEFAULT_ITEM, JoinList.DEFAULT_DELIMITER);
    }

    public JoinList(final ConfigurationSection config, final String prefix, final String defaultFormat, final String defaultItem, final String defaultDelimiter) {
        this(config.getString(prefix + JoinList.CONFIG_KEY_FORMAT, defaultFormat)
                , config.getString(prefix + JoinList.CONFIG_KEY_ITEM, defaultItem)
                , config.getString(prefix + JoinList.CONFIG_KEY_DELIMITER, defaultDelimiter));
    }

    public boolean add(final Object... arguments) {
        return this.add((Object) arguments);
    }

    public String join() {
        final Iterator<E> i = this.iterator();
        if (!i.hasNext()) return MessageFormat.format(this.format, "");

        final StringBuilder items = new StringBuilder();
        while (i.hasNext()) {
            final Object o = i.next();

            // prevent recursion
            if (o == this) {
                items.append("{this}");
                continue;
            }

            // format item, which could either be an array of objects or a single object
            final MessageFormat message = new MessageFormat(this.item);
            final Object[] arguments = ( o instanceof Object[] ? (Object[]) o : new Object[] { o } );
            final StringBuffer sb = message.format(arguments, new StringBuffer(), new FieldPosition(0));
            items.append(sb);

            if (i.hasNext()) items.append(this.delimiter);
        }

        return MessageFormat.format(this.format, items);
    }

    @Override
    public String toString() {
        return this.join();
    }

}
