var app = angular.module("myApp", []);

app.controller("myCtrl", function ($scope, $http, $timeout) {
  $scope.token = "";

  function sendRequestWithToken(url, data, method) {
    var headers = {
      Authorization: "Bearer " + $scope.token,
    };

    return $http({
      url: url,
      method: method,
      data: data,
      headers: headers,
    });
  }

  $scope.getToken = function () {
    $http
      .get("/checkLogin")
      .then(function (response) {
        $scope.token = response.data.accessToken;
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };
  $scope.getToken();

  $scope.inputQuantities = {};
  $scope.isLoading = true;
  $scope.show = true;
  $scope.quantityDetail = 1;
  $timeout(function () {
    $scope.isLoading = false;
  }, 1500);

  var totalAmountElement = document.getElementById("tongSoSanPham").innerHTML;
  if (totalAmountElement > 0) {
    $scope.show = false;
  }

  function formatCurrency(amount) {
    var formattedAmount = amount.toLocaleString("vi-VN");
    return formattedAmount.replace(/\./g, ",");
  }

  $scope.loadGioHang = function () {
    sendRequestWithToken("/giohanginfo", null, "GET")
      .then(function (response) {
        var tongSoLuong = response.data.tongSoSanPham;
        var tongTien = response.data.tongTien;
        var totalAmountElement = document.getElementById("tongSoSanPham");
        if (totalAmountElement) {
          totalAmountElement.textContent = tongSoLuong;
        }
        var totalAmountElements = document.getElementsByClassName("tongTien");
        for (var i = 0; i < totalAmountElements.length; i++) {
          totalAmountElements[i].textContent = formatCurrency(tongTien) + " ₫";
        }
        if (tongSoLuong == 0) {
          $scope.show = true;
        }
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };

  $scope.findColorId = function (mauSacId) {
    sendRequestWithToken("/findColorId?colorId=" + mauSacId, null, "GET")
      .then(function (response) {
        var rowToRemove = document.querySelector(
          '[product-subtotal="' + mauSacId + '"]'
        );
        $scope.inputQuantities[mauSacId] = response.data.soLuong;
        rowToRemove.textContent = formatCurrency(response.data.tongGia) + " ₫";
        $scope.loadGioHang();
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };

  $scope.addToCart = function (colorId, quantity) {
    console.log(colorId);
    sendRequestWithToken(
      "/add?colorId=" + colorId + "&quantity=" + quantity,
      null,
      "POST"
    )
      .then(function (response) {
        if (response.data.message.includes("giỏ hàng")) {
          createToast("success", "Thêm vào giỏ hàng thành công!");
        } else {
          createToast("error", "Sản phẩm đã hết hàng!");
        }
        $scope.loadGioHang();
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };

  $scope.updateCart = function (mauSacId) {
    var colorId = mauSacId;
    var quantity = $scope.inputQuantities[mauSacId];
    if (quantity > 0) {
      sendRequestWithToken(
        "/update?colorId=" + colorId + "&quantity=" + quantity,
        null,
        "POST"
      )
        .then(function (response) {
          $scope.findColorId(colorId);
        })
        .catch(function (error) {
          console.error("Error fetching data:", error);
        });
    } else if (quantity != "" && quantity <= 0) {
      $scope.removeItem(colorId);
    }
  };

  $scope.updateDI = function (mauSacId, quantity) {
    var colorId = mauSacId;
    var quantity = quantity;
    if (quantity > 0) {
      sendRequestWithToken(
        "/update?colorId=" + colorId + "&quantity=" + quantity,
        null,
        "POST"
      )
        .then(function (response) {
          $scope.findColorId(colorId);
        })
        .catch(function (error) {
          console.error("Error fetching data:", error);
        });
    } else if (quantity != "" && quantity < 0) {
      $scope.removeItem(colorId);
    }
  };

  $scope.deleteCart = function (colorId) {
    sendRequestWithToken("/delete?colorId=" + colorId, null, "POST")
      .then(function (response) {
        $scope.loadGioHang();
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };

  $scope.removeItem = function (mauSacId) {
    var rowToRemove = document.querySelector(
      '[data-product-id="' + mauSacId + '"]'
    );
    if (rowToRemove) {
      $scope.deleteCart(mauSacId);
      rowToRemove.remove();
    }
  };

  $scope.decreaseQuantity = function (mauSacId) {
    $scope.updateDI(mauSacId, $scope.inputQuantities[mauSacId] - 1);
  };

  $scope.increaseQuantity = function (mauSacId) {
    $scope.updateDI(mauSacId, $scope.inputQuantities[mauSacId] + 1);
    console.log("Hello");
  };

  $(document).on("click", ".nice-select .option:not(.disabled)", function (t) {
    var s = $(this),
      n = s.closest(".nice-select");
    $("#detailAdd").attr("data-color-id", s.data("value"));
    $(".sm-image[data-mau-sac-id='" + s.data("value") + "']").click();
  });

  $scope.decreaseDetailQuantity = function () {
    if ($scope.quantityDetail > 1) {
      $scope.quantityDetail--;
    }
  };

  $scope.increaseDetailQuantity = function () {
    $scope.quantityDetail++;
  };

  $scope.addDetailCart = function () {
    var colorId = $("#detailAdd").attr("data-color-id");
    var quantity = $scope.quantityDetail;
    $scope.addToCart(colorId, quantity);
    $scope.loadGioHang();
    console.log("Hello");
  };

  $("#checkoutBtn").on("click", function (event) {
    event.preventDefault();
    var selectedPaymentMethod = $("#selectedPaymentMethod").val();
    var formAction = $("form").attr("action");
    formAction =
      "/user/checkout?phuongThucThanhToanId=" + selectedPaymentMethod;
    $("form").attr("action", formAction);
    $("#paymentNow").click();
  });
});

function changeSortBy(value) {
  if (value == "") {
    var url = window.location.href;
    var separator = url.indexOf("?") !== -1 ? "&" : "?";

    if (url.includes("sortBy=")) {
      url = url.replace(/(sortBy=)[^\&]+/, "");
      if (url.endsWith("?") || url.endsWith("&")) {
        url = url.slice(0, -1); // Remove trailing '?' or '&'
      }
    }

    window.location.href = url;
    return;
  }
  var url = window.location.href;
  var separator = url.indexOf("?") !== -1 ? "&" : "?";
  if (url.includes("sortBy=")) {
    url = url.replace(/(sortBy=)[^\&]+/, "$1" + value);
  } else {
    url = url + separator + "sortBy=" + value;
  }
  window.location.href = url;
}
function updateSelectedPaymentMethod(element, paymentMethodId) {
  const selectedPaymentMethod = document.getElementById(
    "selectedPaymentMethod"
  );
  selectedPaymentMethod.value = paymentMethodId;
  var cardElements = document.querySelectorAll(".card");
  cardElements.forEach(function (card) {
    card.classList.remove("actives");
  });

  element.closest(".card").classList.add("actives");
}
$("input[type='password']")
  .on("focus", function () {
    $(this).prop("type", "text");
  })
  .on("blur", function () {
    $(this).prop("type", "password");
  });
