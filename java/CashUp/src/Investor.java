import java.util.HashMap;
import java.util.Map;

public class Investor {

    Investor(final String name, final Double investment) {
        this.name = name;
        this.investment = investment;
    }

    private final String name;
    private final Double investment;
    private Double delta;
    private final Map<Investor, Double> transactions = new HashMap<>();

    public String getName(){
        return name;
    }

    public Double getInvestment() {
        return investment;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(final Double delta) {
        this.delta = delta;
    }

    public void putTransaction(final Investor peer, final Double amount) {
        transactions.put(peer, amount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Investor {name: ").append(name);
        sb.append(", investment: ").append(investment);
        sb.append(", delta: ").append(delta);
        sb.append(", transactions: [");
        transactions.forEach((i, a) -> sb.append("{peer: ").append(i.name)
                .append(", payableAmount: ").append(String.valueOf(a)).append("}"));
        sb.append("]}");
        return sb.toString();
    }

    public void show() {
        System.out.println(this);
    }

}
