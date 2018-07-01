function retrieveQuotes() {
    $.get("/pnl").done(function (data) {
        $('#quotes').empty();
        data.forEach(function(q) {
           $('#quotes').append(
               "<li class='list-group-item'>" +
               "  <strong>" + q.quantity + "x [" + q.name + "]</strong>" +
               "  <strong><div style='float: right'>" + q.pnl + " " + q.currency + "</div></strong>" +
               "  <br />" +
               "  <table>" +
               "    <tr>" +
               "      <td width='50px'>- buy: </td>" +
               "      <td>" + q.buyPrice + " " + q.currency + "</td>" +
               "    </tr>" +
               "    <tr>" +
               "      <td>- " + q.type + ": </td>" +
               "      <td>" + q.currentPrice + " " + q.currency + "</td>" +
               "    </tr>" +
               "  </table>" +
               "  <button type='button' class='btn btn-danger btn-sm' onClick='removeQuote(\"" + q.isin + "\");'>Remove</button> " +
               "</li>"
           );
        });
    });
}

function registerQuote(isin, price, quantity, buyFee, sellFee) {
    $.post("/pnl/" + isin + "/" + price + "/" + quantity + "/" + buyFee + "/" + sellFee).done(function () {
        $('#registerQuote').modal('hide');
        retrieveQuotes();
    })
}

function removeQuote(isin) {
    $.ajax({
        url: "/pnl/" + isin,
        method: "DELETE"
    }).done(function() {
        retrieveQuotes();
    });
}

function addQuote() {
    registerQuote($('#isin').val(), $('#price').val(), $('#quantity').val(), $('#buyFee').val(), $('#sellFee').val());
}

retrieveQuotes();

// Reload every 60 s
setInterval(function() {
    retrieveQuotes();
}, 60000);