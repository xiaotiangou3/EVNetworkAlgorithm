// Smooth scrolling for navigation links
document.querySelectorAll('nav a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        
        const targetId = this.getAttribute('href');
        if (targetId === '#') return;
        
        const targetElement = document.querySelector(targetId);
        if (targetElement) {
            window.scrollTo({
                top: targetElement.offsetTop - 80, // Offset for fixed navbar
                behavior: 'smooth'
            });
        }
    });
});

// Intersection Observer for fade-in animations on scroll
document.addEventListener('DOMContentLoaded', () => {
    const observerOptions = {
        root: null,
        rootMargin: '0px',
        threshold: 0.15
    };

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                // Optional: Stop observing once it's visible
                // observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    const fadeElements = document.querySelectorAll('.fade-in');
    fadeElements.forEach(el => observer.observe(el));
    
    // Add glowing orb mouse tracking effect
    const background = document.querySelector('.background-effects');
    document.addEventListener('mousemove', (e) => {
        const mouseX = e.clientX / window.innerWidth;
        const mouseY = e.clientY / window.innerHeight;
        
        background.style.transform = `translate(${mouseX * -20}px, ${mouseY * -20}px)`;
    });
});

// --- Interactive Trace Logic ---
const INF = "&infin;";

const traceData = {
    "tc1": [
        { i: "-", dist: "-", cst: "-", dp: [0, INF, INF, INF, INF, INF], deque: [0], exp: "Initialization: Start node added to deque. dp[0] = 0.", hlDp: [0], hlDq: [0] },
        { i: 1, dist: 200, cst: 50, dp: [0, 50, INF, INF, INF, INF], deque: [0, 1], exp: "i=1: 200 <= 400 so deque is valid. bestParent is 0. dp[1] = dp[0]+50 = 50. Added 1 to deque.", hlDp: [1], hlDq: [1] },
        { i: 2, dist: 400, cst: 100, dp: [0, 50, 100, INF, INF, INF], deque: [0, 1, 2], exp: "i=2: 400 <= 400. bestParent is 0. dp[2] = dp[0]+100 = 100. Added 2 to deque.", hlDp: [2], hlDq: [2] },
        { i: 3, dist: 600, cst: 50, dp: [0, 50, 100, 100, INF, INF], deque: [1, 3], exp: "i=3: 600-0 > 400, pop 0. bestParent is 1. dp[3] = dp[1]+50 = 100. dp[2] >= dp[3], pop 2 to maintain monotonic property. Add 3 to deque.", hlDp: [3], hlDq: [0, 1] },
        { i: 4, dist: 800, cst: 100, dp: [0, 50, 100, 100, 200, INF], deque: [3, 4], exp: "i=4: 800-200 > 400, pop 1. bestParent is 3. dp[4] = dp[3]+100 = 200. Added 4 to deque.", hlDp: [4], hlDq: [0, 1] },
        { i: 5, dist: 1000, cst: 0, dp: [0, 50, 100, 100, 200, 100], deque: [3, 5], exp: "i=5 (Destination): 1000-600 <= 400. bestParent is 3. dp[5] = dp[3]+0 = 100. dp[4] >= dp[5], pop 4. Add 5. Final output is dp[5] = 100.", hlDp: [5], hlDq: [1] }
    ],
    "tc2": [
        { i: "-", dist: "-", cst: "-", dp: [0, INF, INF, INF, INF, INF], deque: [0], exp: "Initialization: Start node added to deque.", hlDp: [0], hlDq: [0] },
        { i: 1, dist: 200, cst: 50, dp: [0, 50, INF, INF, INF, INF], deque: [0, 1], exp: "i=1: Valid range. dp[1] = dp[0]+50 = 50. Added 1 to deque.", hlDp: [1], hlDq: [1] },
        { i: 2, dist: 300, cst: 50, dp: [0, 50, 50, INF, INF, INF], deque: [0, 1, 2], exp: "i=2: Valid range. dp[2] = dp[0]+50 = 50. Added 2 to deque.", hlDp: [2], hlDq: [2] },
        { i: 3, dist: 800, cst: 50, dp: [0, 50, 50, INF, INF, INF], deque: [], exp: "i=3: Gap exceeds R_max. 800-0 > 400 (pop 0). 800-200 > 400 (pop 1). 800-300 > 400 (pop 2). Deque is empty! Return -1.", hlDp: [], hlDq: [] }
    ]
};

const traceState = {
    "tc1": 0,
    "tc2": 0
};

function renderTrace(tc) {
    const stepIdx = traceState[tc];
    const data = traceData[tc][stepIdx];
    
    document.getElementById(`${tc}-step`).textContent = `Step ${stepIdx} of ${traceData[tc].length - 1}`;
    document.getElementById(`${tc}-explanation`).textContent = data.exp;
    document.getElementById(`${tc}-i`).innerHTML = data.i;
    document.getElementById(`${tc}-dist`).innerHTML = data.dist;
    document.getElementById(`${tc}-cst`).innerHTML = data.cst;
    
    const dpContainer = document.getElementById(`${tc}-dp-array`);
    dpContainer.innerHTML = "";
    data.dp.forEach((val, idx) => {
        const box = document.createElement("div");
        box.className = "array-box" + (data.hlDp.includes(idx) ? " highlight" : "");
        box.innerHTML = `<span class="array-index">${idx}</span>${val}`;
        dpContainer.appendChild(box);
    });
    
    const dqContainer = document.getElementById(`${tc}-deque-array`);
    dqContainer.innerHTML = "";
    data.deque.forEach((val, idx) => {
        const box = document.createElement("div");
        box.className = "array-box" + (data.hlDq.includes(idx) ? " highlight" : "");
        box.innerHTML = `<span class="array-index">${idx}</span>${val}`;
        dqContainer.appendChild(box);
    });
}

function nextStep(tc) {
    if (traceState[tc] < traceData[tc].length - 1) {
        traceState[tc]++;
        renderTrace(tc);
    }
}

function prevStep(tc) {
    if (traceState[tc] > 0) {
        traceState[tc]--;
        renderTrace(tc);
    }
}

// Initial render
document.addEventListener("DOMContentLoaded", () => {
    renderTrace("tc1");
    renderTrace("tc2");
});

