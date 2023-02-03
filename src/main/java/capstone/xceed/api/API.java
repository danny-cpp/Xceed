package capstone.xceed.api;

public interface API {
    public enum T1 {

        // CC to nodeE
        /**
         * Handshake calls
         */
        REQUEST_HANDSHAKE,
        RECEIVED_ID,
        SET_APPROVE_TO_COMMUNICATE,
        SYSTEM_RESET,

        /**
         * Data exchange
         */
        ENCRYPT,
        DECRYPT,

        // nodeE to CC
        SEND_ID,
        HS_COMPLETE,
        RAISE_ERROR,
        SEND_ENCRYPTED,
        SEND_DECRYPTED;


    }
}
