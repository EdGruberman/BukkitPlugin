package edgruberman.bukkit.bukkitplugin.commands.util;

public class StringParameter extends Parameter<String> {

    public StringParameter(final StringParameter.Factory factory) {
        super(factory);
    }

    @Override
    public String parseParameter(final ExecutionRequest request) throws ArgumentContingency {
        return request.getArgument(this.index);
    }





    public static class Factory extends Parameter.Factory<StringParameter, String, StringParameter.Factory> {

        public static StringParameter.Factory create(final String name) {
            return new StringParameter.Factory().setName(name);
        }

        @Override
        public StringParameter build() {
            return new StringParameter(this);
        }

        @Override
        public StringParameter.Factory cast() {
            return this;
        }

    }

}
