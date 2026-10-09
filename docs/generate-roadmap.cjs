const fs = require('node:fs');
const path = require('node:path');

const weeks = [
  {
    title: '01  CONTAINER / AOP / TESTING',
    color: '#167D70',
    tint: '#E6F4EF',
    days: [
      ['Container basics', 'Context + bean lifecycle\nScopes: prototype trap'],
      ['Dependency injection', 'Constructor / setter / field\nQualifier + Primary'],
      ['Config + components', '@Bean + scanning\nStereotypes + properties'],
      ['AOP concepts', 'JDK vs CGLIB proxies\nAdvice types'],
      ['AOP pointcuts', 'Write 4-5 pointcuts\nAdd a logging aspect'],
      ['Testing basics', 'Boot tests + mocks\nMVC / JPA test slices'],
      ['Consolidate', 'Test what you built\nReview shaky concepts'],
    ],
  },
  {
    title: '02  DATA / TRANSACTIONS / BOOT',
    color: '#B85932',
    tint: '#FFF0E5',
    days: [
      ['JDBC template', 'RowMapper\nException translation'],
      ['Propagation', 'REQUIRED / REQUIRES_NEW\nNESTED; take your time'],
      ['Isolation + rollback', 'Rollback rules\nSelf-invocation pitfall'],
      ['Spring Data JPA', 'Repositories + @Query\nPagination / sorting'],
      ['Auto-configuration', 'Conditional beans\nStarters + enablement'],
      ['External config', 'Properties + profiles\nActuator endpoints'],
      ['Consolidate', 'Boot + persistence\nTrigger and fix tx bug'],
    ],
  },
  {
    title: '03  MVC / SECURITY / EXAM PREP',
    color: '#42699B',
    tint: '#EAF1FA',
    days: [
      ['MVC controllers', 'REST + request mapping\nBody / path / params'],
      ['Validation', '@Valid + custom rules\n@ControllerAdvice'],
      ['Security basics', 'Filter chain + users\n@PreAuthorize'],
      ['Spring Academy', 'Fill knowledge gaps\nRevisit weak spots'],
      ['Mock exam 1', 'Review every mistake\nNot just the score'],
      ['Mock exam 2', 'Re-drill weak areas\nFrom exam 1'],
      ['Final exam + review', 'Mock exam 3\nPropagation + scopes'],
    ],
  },
];

const elements = [];
let nextId = 0;

function base(type, x, y, width, height, options = {}) {
  const id = `roadmap-${++nextId}`;
  return {
    id, type, x, y, width, height, angle: 0,
    strokeColor: options.strokeColor ?? '#233633',
    backgroundColor: options.backgroundColor ?? 'transparent',
    fillStyle: 'solid', strokeWidth: options.strokeWidth ?? 1,
    strokeStyle: 'solid', roughness: options.roughness ?? 0,
    opacity: 100, groupIds: [], frameId: null,
    roundness: type === 'rectangle' ? { type: 3 } : null,
    seed: nextId * 7919, version: 1, versionNonce: nextId * 104729,
    isDeleted: false, boundElements: null, updated: 1,
    link: null, locked: false,
  };
}

function rectangle(x, y, width, height, fill, stroke = 'transparent') {
  elements.push(base('rectangle', x, y, width, height, {
    strokeColor: stroke, backgroundColor: fill,
  }));
}

function text(value, x, y, width, height, size, color, options = {}) {
  elements.push({
    ...base('text', x, y, width, height, { strokeColor: color }),
    text: value, fontSize: size, fontFamily: 1,
    textAlign: options.align ?? 'left', verticalAlign: 'middle',
    containerId: null, originalText: value, autoResize: false,
    lineHeight: 1.25,
  });
}

const canvas = '#F8FAF7';
const ink = '#21332F';
const cardWidth = 262;
const cardHeight = 180;
const gap = 18;
const left = 70;

text('SPRING CERTIFICATION', left, 40, 1150, 58, 42, ink);
text('21 DAYS  /  ONE RUNNING APPLICATION  /  2 HOURS A DAY', left, 104, 1350, 32, 20, '#52645F');
rectangle(left, 151, 1942, 3, '#D9E4DE');

weeks.forEach((week, weekIndex) => {
  const weekY = 184 + weekIndex * 295;
  rectangle(left, weekY, 8, 40, week.color);
  text(week.title, left + 22, weekY, 1200, 42, 28, week.color);
  text(`DAYS ${weekIndex * 7 + 1}-${weekIndex * 7 + 7}`,
    1770, weekY + 5, 242, 31, 17, week.color, { align: 'right' });

  week.days.forEach(([title, detail], dayIndex) => {
    const day = weekIndex * 7 + dayIndex + 1;
    const x = left + dayIndex * (cardWidth + gap);
    const y = weekY + 62;
    const checkpoint = day === 7 || day === 14 || day >= 19;
    const fill = checkpoint ? '#FFF0CF' : week.tint;
    const accent = checkpoint ? '#A26C13' : week.color;

    rectangle(x, y, cardWidth, cardHeight, fill, '#D5DFDA');
    rectangle(x, y, cardWidth, 6, accent);
    text(`DAY ${String(day).padStart(2, '0')}`, x + 15, y + 17, 125, 25, 18, accent);
    if (checkpoint) {
      text(day === 7 || day === 14 ? 'RECAP' : 'EXAM',
        x + 162, y + 17, 84, 25, 15, accent, { align: 'right' });
    }
    text(title, x + 15, y + 51, cardWidth - 30, 44, 21, ink);
    text(detail, x + 15, y + 105, cardWidth - 30, 61, 16, '#485B55');
  });
});

rectangle(left, 1096, 1942, 68, '#E7EEE9');
text('DAILY RHYTHM', left + 18, 1110, 205, 38, 19, ink);
text('~1 hour theory  +  ~1 hour hands-on', left + 230, 1110, 720, 38, 20, ink);
text('REVISIT: bean scopes + transaction propagation', 1190, 1110, 804, 38, 18, '#A26C13', { align: 'right' });

const scene = {
  type: 'excalidraw', version: 2, source: 'https://excalidraw.com',
  elements,
  appState: { viewBackgroundColor: canvas, gridSize: null },
  files: {},
};

fs.writeFileSync(
  path.join(__dirname, '21-day-roadmap.excalidraw'),
  `${JSON.stringify(scene, null, 2)}\n`,
);