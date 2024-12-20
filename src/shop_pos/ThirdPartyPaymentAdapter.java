/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop_pos;

/**
 *
 * @author Hassan
 */

    
public class ThirdPartyPaymentAdapter implements PaymentProcessor {
    private final ThirdPartyPayment thirdPartyPayment;

    public ThirdPartyPaymentAdapter(ThirdPartyPayment thirdPartyPayment) {
        this.thirdPartyPayment = thirdPartyPayment;
    }

    public void processPayment(double amount) {
        thirdPartyPayment.pay(amount);
    }

    private static class ThirdPartyPayment {

        public ThirdPartyPayment() {
        }

        private void pay(double amount) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
