// ĐỐI VỚI CÂU HỎI THƯỜNG GẶP
jQuery(document).ready(function($){
    
    var panels = $(".faq-ans").hide();

    panels.first().show();
    
     $(".faq-que").click(function(){

         var $this = $(this);

         panels.slideUp();
         $this.next().slideDown();
         
    });

});

// ĐỐI VỚI DANH SÁCH ĐƠN HÀNG
jQuery(document).ready(function($){
    
    var panels = $(".orderlist-body").hide();

    panels.first().show();
    
     $(".orderlist-head").click(function(){

         var $this = $(this);

         panels.slideUp();
         $this.next().slideDown();
         
    });

});