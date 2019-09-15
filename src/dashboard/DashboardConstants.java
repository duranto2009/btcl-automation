package dashboard;

public class DashboardConstants {
    enum Icons {
        ICON_MONEY ("fa fa-money"),
        ICON_USER ("icon-user"),

        ;

        private String iconName;
        Icons(String icon) {
            this.iconName = icon;
        }
        public String getIconName() {
            return this.iconName;
        }
    }
    enum TextClass {
        GREEN_SHARP("font-green-sharp"),
        RED_HAZE("font-red-haze"),
        BLUE_SHARP ("font-blue-sharp"),
        PURPLE_SOFT("font-purple-soft")
        ;


        TextClass(String textClass) {
            this.textClass = textClass;
        }

        private String textClass;
        public String getTextClass() {
            return textClass;
        }


    }
}
