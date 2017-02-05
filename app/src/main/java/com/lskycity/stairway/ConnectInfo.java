package com.lskycity.stairway;

/**
 * Created by zhaofliu on 10/10/16.
 */
class ConnectInfo {
    String sername;
    String ip;
    String username;
    String password;

    public ConnectInfo(String sername, String ip, String username, String password) {
        this.sername = sername;
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectInfo message = (ConnectInfo) o;

        if (sername != null ? !sername.equals(message.sername) : message.sername != null)
            return false;
        if (ip != null ? !ip.equals(message.ip) : message.ip != null) return false;
        if (username != null ? !username.equals(message.username) : message.username != null)
            return false;
        return password != null ? password.equals(message.password) : message.password == null;

    }

    @Override
    public int hashCode() {
        int result = sername != null ? sername.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sername='" + sername + '\'' +
                ", ip='" + ip + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
