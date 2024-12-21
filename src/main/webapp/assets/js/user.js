jQuery( document ).ready(function( $ ) {
	"use strict";
	
	// Kiểm tra nếu user có trong session
	    function setupLogoutTimer() {
	        let timeout;
	        let timeLimit = 60 * 60 * 1000;

	        function resetTimer() {
	            clearTimeout(timeout);
	            timeout = setTimeout(function () {
	                // Hiển thị hộp thoại xác nhận
	                $('#logout-modal').fadeIn();
	                $('#overlay').fadeIn();

	                // Xử lý khi người dùng bấm "Xác nhận"
	                $('#confirm-logout').on('click', function () {
	                    window.location.href = '/btck-atbmhttt/LogoutServlet';
	                }); 
	            }, timeLimit);
	        }

	        // Đặt sự kiện reset timer khi người dùng thao tác
	        window.onload = resetTimer;
	        document.onmousemove = resetTimer;
	        document.onkeypress = resetTimer;
	        document.onscroll = resetTimer;
	    }

	    // Kiểm tra nếu session "user" tồn tại
	    if (window.hasSessionUser) {
	        setupLogoutTimer();
	    }
		
		// Sự kiện hiển thị menu user dropdown
	    $('#user-icon-button').click(function () {
	        const userDropdown = $('#user-dropdown');
	        userDropdown.toggle(); // Hiển thị hoặc ẩn menu
	    });
		
		// Kiểm tra và xử lý khi nhấn vào lang-icon-button
		$('#lang-icon-button').click(function (event) {
		    const langDropdown = $('#lang-dropdown');
		    langDropdown.toggle(); // Hiển thị hoặc ẩn menu
		});

	    // Ẩn menu khi click ra ngoài
		$(document).click(function (event) {
		    if (!$(event.target).closest('#user-icon-button, #user-dropdown').length) {
		        $('#user-dropdown').hide(); // Hide user dropdown
		    }
		});
		
		// Ẩn lang-dropdown khi click ngoài
	    $(document).click(function (event) {
	        if (!$(event.target).closest('#lang-icon-button, #lang-dropdown').length) {
	            $('#lang-dropdown').hide(); // Ẩn dropdown nếu click ngoài
	        }
	    });

		
        $(function() {
            $( "#tabs" ).tabs();
        });

        // Page loading animation
        $("#preloader").animate({
            'opacity': '0'
        }, 600, function(){
            setTimeout(function(){
                $("#preloader").css("visibility", "hidden").fadeOut();
            }, 300);
        });
        

        $(window).scroll(function() {
          var scroll = $(window).scrollTop();
          var box = $('.header-text').height();
          var header = $('header').height();

          if (scroll >= box - header) {
            $("header").addClass("background-header");
          } else {
            $("header").removeClass("background-header");
          }
        });
        if ($('.owl-clients').length) {
            $('.owl-clients').owlCarousel({
                loop: true,
                nav: false,
                dots: true,
                items: 1,
                margin: 30,
                autoplay: false,
                smartSpeed: 700,
                autoplayTimeout: 6000,
                responsive: {
                    0: {
                        items: 1,
                        margin: 0
                    },
                    460: {
                        items: 1,
                        margin: 0
                    },
                    576: {
                        items: 3,
                        margin: 20
                    },
                    992: {
                        items: 5,
                        margin: 30
                    }
                }
            });
        }
		if ($('.owl-testimonials').length) {
            $('.owl-testimonials').owlCarousel({
                loop: true,
                nav: false,
                dots: true,
                items: 1,
                margin: 30,
                autoplay: false,
                smartSpeed: 700,
                autoplayTimeout: 6000,
                responsive: {
                    0: {
                        items: 1,
                        margin: 0
                    },
                    460: {
                        items: 1,
                        margin: 0
                    },
                    576: {
                        items: 2,
                        margin: 20
                    },
                    992: {
                        items: 2,
                        margin: 30
                    }
                }
            });
        }
        if ($('.owl-banner').length) {
            $('.owl-banner').owlCarousel({
                loop: true,
                nav: false,
                dots: true,
                items: 1,
                margin: 0,
                autoplay: false,
                smartSpeed: 700,
                autoplayTimeout: 6000,
                responsive: {
                    0: {
                        items: 1,
                        margin: 0
                    },
                    460: {
                        items: 1,
                        margin: 0
                    },
                    576: {
                        items: 1,
                        margin: 20
                    },
                    992: {
                        items: 1,
                        margin: 30
                    }
                }
            });
        }

        $(".Modern-Slider").slick({
            autoplay:true,
            autoplaySpeed:10000,
            speed:600,
            slidesToShow:1,
            slidesToScroll:1,
            pauseOnHover:false,
            dots:true,
            pauseOnDotsHover:true,
            cssEase:'linear',
           // fade:true,
            draggable:false,
            prevArrow:'<button class="PrevArrow"></button>',
            nextArrow:'<button class="NextArrow"></button>', 
        });

        $('.filters ul li').click(function(){
        $('.filters ul li').removeClass('active');
        $(this).addClass('active');
          
          var data = $(this).attr('data-filter');
          $grid.isotope({
            filter: data
          })
        });

        var $grid = $(".grid").isotope({
          itemSelector: ".all",
          percentPosition: true,
          masonry: {
            columnWidth: ".all"
          }
        })
        $('.accordion > li:eq(0) a').addClass('active').next().slideDown();

        $('.accordion a').click(function(j) {
            var dropDown = $(this).closest('li').find('.content');

            $(this).closest('.accordion').find('.content').not(dropDown).slideUp();

            if ($(this).hasClass('active')) {
                $(this).removeClass('active');
            } else {
                $(this).closest('.accordion').find('a.active').removeClass('active');
                $(this).addClass('active');
            }

            dropDown.stop(false, true).slideToggle();

            j.preventDefault();
        });
 
});
