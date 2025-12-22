# Claude Code Agent Templates

This directory contains template definitions for Claude Code custom agents used in the DoomDoc project.

## Available Agents

### js-specialist
Specialized agent for vanilla JavaScript development, DOM manipulation, and browser compatibility.

**Use cases:**
- Implementing interactive UI features
- Optimizing JavaScript performance
- Reviewing JavaScript code for best practices
- Refactoring vanilla JavaScript

**Location:** `agents/js-specialist.md`

### ui-design-specialist
Specialized agent for CSS design, visual hierarchy, and user interface layout.

**Use cases:**
- Creating and refining CSS stylesheets
- Improving visual hierarchy and information design
- Balancing typography, color, and spacing
- Reviewing UI/UX design decisions

**Location:** `agents/ui-design-specialist.md`

## Using These Templates

To use these agent definitions in your Claude Code session:

1. Copy the desired agent file(s) to your `.claude/agents/` directory:
   ```bash
   mkdir -p .claude/agents
   cp claude-templates/agents/js-specialist.md .claude/agents/
   cp claude-templates/agents/ui-design-specialist.md .claude/agents/
   ```

2. The agents will be automatically available in your Claude Code session.

3. Claude will use these agents when appropriate tasks are identified (as defined in each agent's description).

## Customizing Agents

You can customize these templates for your specific needs:

1. Copy an agent file to `.claude/agents/`
2. Edit the frontmatter (name, description, model, color)
3. Modify the agent's instructions and behavior
4. Save and the changes will take effect in your next Claude Code session

## Note

The `.claude/` directory is gitignored, so your customized agents remain local to your machine.
