<#include "./import/top.ftl">
<#include "./import/navbar.ftl">

<style>
    .bmi-container {
        background: white;
        border-radius: 15px;
        box-shadow: 0 5px 25px rgba(0,0,0,0.1);
        padding: 30px;
        margin: 40px auto;
        max-width: 500px;
        text-align: center;
    }

    .bmi-title {
        color: #2c3e50;
        font-weight: 600;
        margin-bottom: 30px;
        font-size: 28px;
    }

    .bmi-form .form-group {
        margin-bottom: 25px;
        text-align: left;
    }

    .bmi-form label {
        color: #555;
        font-weight: 500;
        margin-bottom: 8px;
        display: block;
    }

    .bmi-form .form-control {
        border: 2px solid #e9ecef;
        border-radius: 8px;
        padding: 12px 15px;
        font-size: 16px;
        transition: all 0.3s ease;
        width: 100%;
        box-sizing: border-box;
    }

    .bmi-form .form-control:focus {
        border-color: #3498db;
        box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
    }

    .bmi-btn {
        background: linear-gradient(135deg, #3498db, #2980b9);
        border: none;
        border-radius: 8px;
        padding: 14px;
        font-size: 16px;
        font-weight: 600;
        color: white;
        transition: all 0.3s ease;
        margin-top: 10px;
        width: 100%;
        cursor: pointer;
    }

    .bmi-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(52, 152, 219, 0.3);
    }

    .bmi-result {
        border-radius: 10px;
        padding: 25px;
        margin-top: 25px;
        text-align: center;
        display: none;
        animation: fadeIn 0.5s ease;
        border: 2px solid transparent;
    }

    .bmi-value {
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 10px;
    }

    .bmi-status {
        font-size: 20px;
        font-weight: 500;
    }

    .reference-card {
        background: white;
        border-radius: 15px;
        box-shadow: 0 5px 25px rgba(0,0,0,0.1);
        padding: 25px;
        margin: 30px auto;
        max-width: 500px;
    }

    .reference-title {
        color: #2c3e50;
        font-weight: 600;
        margin-bottom: 20px;
        text-align: center;
        font-size: 20px;
    }

    .reference-list {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .reference-list li {
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
        display: flex;
        align-items: center;
        margin-bottom: 8px;
    }

    .reference-list li:last-child {
        border-bottom: none;
        margin-bottom: 0;
    }

    .reference-badge {
        padding: 8px 15px;
        border-radius: 20px;
        font-weight: 600;
        margin-right: 15px;
        min-width: 90px;
        text-align: center;
        font-size: 14px;
    }

    .reference-text {
        font-size: 16px;
        color: #555;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }

    /* 确保容器正确显示 */
    .container {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 15px;
    }
</style>

<div class="container">
    <div class="bmi-container">
        <h1 class="bmi-title">BMI 计算器</h1>

        <form id="bmiForm" class="bmi-form">
            <div class="form-group">
                <label for="height">身高 (cm)</label>
                <input type="number" class="form-control" id="height" name="height"
                       placeholder="请输入身高" step="0.1" min="0" required>
            </div>
            <div class="form-group">
                <label for="weight">体重 (kg)</label>
                <input type="number" class="form-control" id="weight" name="weight"
                       placeholder="请输入体重" step="0.1" min="0" required>
            </div>
            <button type="submit" class="bmi-btn">计算 BMI</button>
        </form>

        <div id="result" class="bmi-result">
            <div class="bmi-value" id="bmiValue"></div>
            <div class="bmi-status" id="bmiStatus"></div>
        </div>
    </div>

    <div class="reference-card">
        <h3 class="reference-title">BMI 标准参考</h3>
        <ul class="reference-list">
            <li>
                <span class="reference-badge" style="background: #ffeb3b; color: #333;">＜18.5</span>
                <span class="reference-text">体重过轻</span>
            </li>
            <li>
                <span class="reference-badge" style="background: #4caf50; color: white;">18.5-23.9</span>
                <span class="reference-text">正常范围</span>
            </li>
            <li>
                <span class="reference-badge" style="background: #ff9800; color: white;">24-27.9</span>
                <span class="reference-text">超重</span>
            </li>
            <li>
                <span class="reference-badge" style="background: #f44336; color: white;">≥28</span>
                <span class="reference-text">肥胖</span>
            </li>
        </ul>
    </div>
</div>

<script>
    document.getElementById('bmiForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const height = parseFloat(document.getElementById('height').value);
        const weight = parseFloat(document.getElementById('weight').value);

        console.log('输入值:', { height, weight }); // 调试信息

        if (height > 0 && weight > 0) {
            const heightInMeter = height / 100;
            const bmi = weight / (heightInMeter * heightInMeter);
            const bmiRounded = bmi.toFixed(2);

            console.log('计算结果:', bmiRounded); // 调试信息

            const resultDiv = document.getElementById('result');
            const bmiValue = document.getElementById('bmiValue');
            const bmiStatus = document.getElementById('bmiStatus');

            bmiValue.textContent = 'BMI:'+bmiRounded ;

            let status = '';
            let bgColor = '';
            let textColor = '';
            let borderColor = '';

            if (bmi < 18.5) {
                status = '体重过轻';
                bgColor = '#fff8e1';
                textColor = '#ff8f00';
                borderColor = '#ffd54f';
            } else if (bmi >= 18.5 && bmi < 24) {
                status = '正常范围';
                bgColor = '#e8f5e8';
                textColor = '#2e7d32';
                borderColor = '#81c784';
            } else if (bmi >= 24 && bmi < 28) {
                status = '超重';
                bgColor = '#fff3e0';
                textColor = '#ef6c00';
                borderColor = '#ffb74d';
            } else {
                status = '肥胖';
                bgColor = '#ffebee';
                textColor = '#c62828';
                borderColor = '#ef5350';
            }

            bmiStatus.textContent = status;
            resultDiv.style.display = 'block';
            resultDiv.style.backgroundColor = bgColor;
            resultDiv.style.color = textColor;
            resultDiv.style.borderColor = borderColor;

            console.log('显示结果:', status); // 调试信息
        } else {
            alert('请输入有效的身高和体重数值！');
        }
    });

    // 添加输入框实时验证
    document.getElementById('height').addEventListener('input', function() {
        if (this.value < 0) this.value = '';
    });

    document.getElementById('weight').addEventListener('input', function() {
        if (this.value < 0) this.value = '';
    });
</script>

<#include "./import/viewBottom.ftl">