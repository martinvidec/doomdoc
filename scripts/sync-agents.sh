#!/bin/bash
# Synchronize agent templates from .claude/agents to claude-templates/agents
# This ensures that any changes made to active agents are preserved in the repository

set -e

SOURCE_DIR=".claude/agents"
TEMPLATE_DIR="claude-templates/agents"

# Check if source directory exists
if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: Source directory $SOURCE_DIR does not exist"
    exit 1
fi

# Create template directory if it doesn't exist
mkdir -p "$TEMPLATE_DIR"

# Sync all .md files from source to template
echo "Synchronizing agents from $SOURCE_DIR to $TEMPLATE_DIR..."

# Copy all .md files
rsync -av --include='*.md' --exclude='*' "$SOURCE_DIR/" "$TEMPLATE_DIR/"

# Check if any files were changed
if git diff --quiet "$TEMPLATE_DIR"; then
    echo "✓ Agent templates are up to date"
else
    echo "✓ Agent templates synchronized"
    echo ""
    echo "Changed files:"
    git diff --name-only "$TEMPLATE_DIR"
    echo ""
    echo "Remember to commit these changes to preserve agent updates!"
fi
