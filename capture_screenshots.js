// Student ID: 2024EB01570
// Script to render and save high-resolution screenshots for all deliverables

const path = require('path');
const fs = require('fs');

async function capture() {
    console.log("Loading puppeteer...");
    const puppeteer = require('puppeteer');

    const browser = await puppeteer.launch({
        headless: 'new',
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    const page = await browser.newPage();
    await page.setViewport({ width: 1400, height: 1800, deviceScaleFactor: 2 });

    const htmlPath = 'file:///' + path.resolve(__dirname, 'render_screenshots.html').replace(/\\/g, '/');
    console.log("Opening page:", htmlPath);
    await page.goto(htmlPath, { waitUntil: 'networkidle0' });

    const deliverablesDir = path.join(__dirname, 'deliverables_screenshots');
    if (!fs.existsSync(deliverablesDir)) {
        fs.mkdirSync(deliverablesDir, { recursive: true });
    }

    const screens = [
        { id: '#screen-register', name: 'Screenshot_1_RegisterActivity.png' },
        { id: '#screen-login', name: 'Screenshot_2_LoginActivity.png' },
        { id: '#screen-welcome', name: 'Screenshot_3_WelcomeActivity.png' },
        { id: '#screen-notice-creation', name: 'Screenshot_4_NoticeCreationScreen.png' },
        { id: '#screen-notices-retrieved', name: 'Screenshot_5_NoticesRetrievedScreen.png' },
        { id: '#screen-notification', name: 'Screenshot_6_ServiceNotification.png' }
    ];

    for (const screen of screens) {
        console.log(`Capturing ${screen.name}...`);
        const element = await page.$(screen.id);
        if (element) {
            const outputPath = path.join(deliverablesDir, screen.name);
            await element.screenshot({ path: outputPath });
            console.log(`Saved: ${outputPath}`);
        } else {
            console.error(`Element not found: ${screen.id}`);
        }
    }

    await browser.close();
    console.log("All screenshots captured successfully!");
}

capture().catch(err => {
    console.error("Error capturing screenshots:", err);
    process.exit(1);
});
