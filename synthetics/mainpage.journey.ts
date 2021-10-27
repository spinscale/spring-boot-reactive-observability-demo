import { journey, step, expect } from '@elastic/synthetics';

journey('check if document is present', ({ page, params }) => {
  step('launch app', async () => {
    await page.goto(params.url);
  });

  step('assert table', async () => {
    const table = await page.$('table');
    expect(await table.textContent()).toContain('Image');
    expect(await table.textContent()).toContain('Name');
    expect(await table.textContent()).toContain('Description');
    expect(await table.textContent()).toContain('Tags');
    expect(await table.textContent()).toContain('My Product');
  });
});

