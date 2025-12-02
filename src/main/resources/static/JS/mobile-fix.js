$(function () {
    // 现有代码保持不变
    let labelClassList = ["label-badge", "label-primary", "label-success", "label-info", "label-warning", "label-danger"];
    let articleTags = $("#articleTagListBox span");
    for (let i = 0; i < articleTags.length; i++) {
        $(articleTags[i]).addClass(labelClassList[Math.floor(Math.random() * labelClassList.length)]);
    }

    // 新增移动端优化代码
    if ('ontouchstart' in window) {
        // 动态添加移动端样式
        const mobileStyles = `
            <style>
                @media (max-width: 768px) {
                    .col-xs-6 { padding: 4px !important; }
                    .card a { min-height: 140px !important; display: block !important; padding: 8px !important; }
                    .list-group-item { min-height: 44px !important; padding: 12px 8px !important; }
                    .list-group-item a { display: block !important; padding: 8px 0 !important; }
                    .label-badge { display: inline-block !important; margin: 4px !important; padding: 8px 12px !important; min-height: 36px !important; }
                    .label-badge a { display: block !important; padding: 4px 8px !important; }
                }
                * { -webkit-tap-highlight-color: transparent !important; touch-action: manipulation !important; }
            </style>
        `;
        $('head').append(mobileStyles);

        // 添加触摸反馈
        $('a').on('touchstart', function() {
            $(this).css('opacity', '0.7');
        }).on('touchend', function() {
            $(this).css('opacity', '1');
        });

        // 防止双击缩放
        let lastTouchEnd = 0;
        document.addEventListener('touchend', function (event) {
            const now = (new Date()).getTime();
            if (now - lastTouchEnd <= 300) {
                event.preventDefault();
            }
            lastTouchEnd = now;
        }, false);
    }
});