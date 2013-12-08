package simcity.bank.test.mock;


public class Mock {
        private String name;

        public Mock(String name) {
                this.name = name;
        }

        public String getName() {
                return name;
        }

        public String toString() {
                return this.getClass().getName() + ": " + name;
        }

        public EventLog log = new EventLog();

}

