
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final public class CashUp {

    private final List<Investor> investors = new ArrayList<>();
    private Double sum;
    private Double average;

    private void print(String toPrint) {
        System.out.println(toPrint);
    }

    private void _init() {
        this.sum = calculateSum();
        this.average = calculateAverage();
        investors.forEach(i -> i.setDelta(i.getInvestment() - average));
    }

    private void addInvestor(final String name, final Double investment) {
        this.investors.add(new Investor(name, investment));
        _init();
    }

    private Double calculateAverage() {
        return investors.stream().collect(Collectors.averagingDouble(Investor::getInvestment));
    }

    private Double calculateSum() {
        return investors.stream().collect(Collectors.summarizingDouble(Investor::getInvestment)).getSum();
    }

    private void cashUp() {
        print("###");
        print("Sum: " + String.valueOf(sum));
        print("Avg: " + String.valueOf(average));
        List<Investor> debtors = investors.stream()
                .filter(i -> i.getDelta() < 0)
                .sorted((i1, i2) -> Double.compare(i1.getDelta(), i2.getDelta()))
                .collect(Collectors.toList());
        List<Investor> creditors = investors.stream()
                .filter(i -> i.getDelta() > 0)
                .sorted((i1, i2) -> Double.compare(i2.getDelta(), i1.getDelta()))
                .collect(Collectors.toList());

        print("Debtors:");
        debtors.forEach(Investor::show);
        print("Creditors:");
        creditors.forEach(Investor::show);

        debtors.forEach((debtor) -> {
            creditors.forEach((creditor) -> {
                print("###");
                print("Current debtor: " + debtor);
                print("Current creditor: " + creditor);
                do {
                    // Guard clause
                    if (debtor.getDelta() == 0.0 || creditor.getDelta() == 0.0) {
                        print("Nothing to do");
                        return;
                    }
                    Double payableToCreditor;
                    if (creditor.getDelta() > 0.0) {
                        // Pay the current creditor all we have on our delta, at most his outstanding delta
                        if (Math.abs(debtor.getDelta()) < Math.abs(creditor.getDelta())) {
                            payableToCreditor = Math.abs(debtor.getDelta());
                        } else {
                            payableToCreditor = Math.abs(creditor.getDelta());
                        }
                        print("Adding transaction {name: " + debtor.getName() + ", peer: " + creditor.getName()
                                + ", amount: " + payableToCreditor + "}");
                        debtor.putTransaction(creditor, payableToCreditor);
                    } else {
                        // This creditor has no delta, so nothing to be done
                        return;
                    }
                    // Delta is negative (or you wouldn't be a debtor), add payable amount to it
                    Double deltaAfterTransaction = debtor.getDelta() + payableToCreditor;
                    print("Delta after transaction: " + deltaAfterTransaction);
                    if (deltaAfterTransaction < 0.0) {
                        // We paid the creditor's full delta and have debt left, meaning we can continue with the next creditor.
                        // Update remaining delta to reflect the paid amount to current creditor
                        debtor.setDelta(deltaAfterTransaction);
                        print("Clearing creditor delta");
                        creditor.setDelta(0.0);
                        return;
                    } else {
                        // We couldn't fully repay the current creditor, making this the first and final transaction for the
                        // current debitor. Since we paid our debts (Our remaining debt is now a positive value), set remaining
                        // debt to 0.0
                        print("Clearing debtor delta");
                        debtor.setDelta(0.0);
                        // Update creditor's delta
                        creditor.setDelta(creditor.getDelta() - payableToCreditor);

                    }
                } while (debtor.getDelta() < 0);
            });
        });
        print("###");
        investors.forEach(Investor::show);
    }

    public static void main(String[] args) {
        final CashUp cu = new CashUp();

        cu.addInvestor("A", 90.0);
        cu.addInvestor("B", 130.0);
        cu.addInvestor("C", 140.0);
        cu.addInvestor("D", 40.0);
        cu.addInvestor("E", 80.0);
        cu.addInvestor("F", 100.0);
        cu.addInvestor("G", 190.0);
        cu.addInvestor("H", 24.0);

        cu.cashUp();
    }

}
