package underlay;

import java.rmi.RemoteException;

/**
 * Represents the underlay layer of the skip-graph DHT. Handles node-to-node communication.
 */
public class Underlay {

    private ConnectionAdapter connectionAdapter;

    /**
     * Constructs the underlay.
     * @param adapter default connection adapter.
     * @param port the port that the adapter should be bound to.
     */
    public Underlay(ConnectionAdapter adapter, int port) {
        // Initialize & register the underlay connection adapter.
        try {
            if(adapter.construct(port)) connectionAdapter = adapter;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Can be used to update the connection adapter of the underlay layer.
     * @param newAdapter new connection adapter.
     * @param port the port that the adapter should be bound to.
     */
    public void setConnectionAdapter(ConnectionAdapter newAdapter, int port) {
        if(connectionAdapter != null) {
            try {
                connectionAdapter.destruct();
                if(newAdapter.construct(port)) connectionAdapter = newAdapter;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Can be used to send a message to a remote server that runs the same underlay architecture.
     * @param address address of the remote server.
     * @param t type of the request.
     * @param p parameters of the request.
     * @return response emitted by the remote server.
     */
    public ResponseParameters sendMessage(String address, RequestType t, RequestParameters p) {
        if(connectionAdapter == null) {
            System.err.println("[Underlay] Adapter does not exist.");
            return null;
        }
        // Connect to the remote adapter.
        ConnectionAdapter remote = null;
        try {
            remote = connectionAdapter.remote(address);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(remote == null) {
            System.err.println("[Underlay] Could not send the message.");
            return null;
        }
        // Transform the request to RMI invocations.
        try {
            switch (t) {
                case SearchByNameID:
                    return remote.searchByNameID((String) p.getRequestValue("targetNameID"));
                case SearchByNumID:
                    return remote.searchByNumID((Integer) p.getRequestValue("targetNumID"));
                case NameIDLevelSearch:
                    return remote.nameIDLevelSearch((Integer) p.getRequestValue("level"),
                            (String) p.getRequestValue("targetNameID"));
                case UpdateLeftNode:
                    return remote.updateLeftNode((Integer) p.getRequestValue("level"),
                            (String) p.getRequestValue("newValue"));
                case UpdateRightNode:
                    return remote.updateRightNode((Integer) p.getRequestValue("level"),
                            (String) p.getRequestValue("newValue"));
            }
        } catch (Exception e) {
            System.err.println();
            return null;
        }
        return null;
    }
}
