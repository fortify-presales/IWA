module.exports = {
    name: 'unread-message-count',
    data: function () {
        return {
            count: 0
        };
    },
    mounted() {
        console.log("Using JWT Bearer token: " + jwtToken);
        axios
            .get("/api/v3/messages/unread-count/" + userId, {'headers': {'Authorization': 'Bearer ' + jwtToken}})
            .then(response => (this.count = response.data))
    }
};
